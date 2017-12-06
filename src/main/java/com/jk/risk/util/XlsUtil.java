package com.jk.risk.util;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.jk.risk.common.domain.InterestRate;
import com.jk.risk.common.domain.InterestRatePreView;
import com.jk.risk.exception.RiskSystemException;
import com.monitorjbl.xlsx.StreamingReader;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
/**
 * 工具类
 * User: allen
 * Date: 2017/11/29 下午10:30
 */
public class XlsUtil {

    private static final Logger logger    = LoggerFactory.getLogger(XlsUtil.class);
    private static final String FILE_NAME = "/export/Data/RM/test-{date}.xls";

    /**
     * 导入文件
     * @param file Excel文件
     * @return 利率list
     * @throws Exception error
     */
    public static List<InterestRate> importInterestRatesFromFile(java.io.File file) throws Exception{
        try (FileInputStream in = new FileInputStream(file)){
            org.apache.poi.ss.usermodel.Workbook wk = StreamingReader.builder()
                    .rowCacheSize(100)      //缓存到内存中的行数，默认是10
                    .bufferSize(4096)       //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(in);              //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            Sheet sheet = wk.getSheetAt(0);
            List<InterestRate> rates = Lists.newArrayList();
            int rowNum = sheet.getLastRowNum();
            for (int i = 1; i <= rowNum; i++){
                InterestRate rate = new InterestRate();
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                rate.yearth = cell.getNumericCellValue();

                cell = row.getCell(2);
                if(cell !=null) rate.basicNationalDebt = cell.getNumericCellValue();

                cell = row.getCell(3);
                if(cell !=null) rate.nationalDebtUp = cell.getNumericCellValue();

                cell = row.getCell(4);
                if(cell !=null) rate.nationalDebtDown = cell.getNumericCellValue();

                cell = row.getCell(5);
                if(cell !=null) rate.AAA = cell.getNumericCellValue();

                cell = row.getCell(6);
                rate.AAPlus = cell.getNumericCellValue();

                cell = row.getCell(7);
                rate.AA = cell.getNumericCellValue();

                cell = row.getCell(8);
                rate.AAMinus = cell.getNumericCellValue();

                cell = row.getCell(9);
                rate.APlus = cell.getNumericCellValue();

                cell = row.getCell(10);
                rate.A = cell.getNumericCellValue();

                cell = row.getCell(11);
                rate.B = cell.getNumericCellValue();

                rates.add(rate);
            }
            return rates;
        }catch (Exception e){
            throw new RiskSystemException("生成[List<InterestRate>]错误, 请检查数据是否正确!");
        }
    }

    /**
     * 导出Excel文件
     * @param list 利率及贴现因子输出
     * @return 文件
     */
    public static String exportRateAndDiscountFactor(List<InterestRatePreView> list) {
        String fileName = "/export/Data/RM/利率风险计算-利率及贴现因子输出.xslx";
        WritableWorkbook wwb = null;
        try {
            File file = new File(fileName);
            if(file.exists()) file.delete();
            if (!file.createNewFile()) {
                logger.error("Failed to create excel file to path {}.", fileName);
                return null;
            }
            wwb = Workbook.createWorkbook(file);
            WritableSheet ws = wwb.createSheet("利率风险计算-利率及贴现因子输出", 0);
            // colums: 期限	类型	情景	等级	值
            Label dateLimit = new Label(0, 0, "期限");
            Label rateType = new Label(1, 0, "类型");
            Label sence = new Label(2, 0, "情景");
            Label levelype = new Label(3, 0, "等级");
            Label value = new Label(4, 0, "值");
            ws.addCell(dateLimit);
            ws.addCell(rateType);
            ws.addCell(sence);
            ws.addCell(levelype);
            ws.addCell(value);

            for(int i = 1; i <= list.size(); i++){
                InterestRatePreView rate = list.get(i);
                Label labelDateLimit_i = new Label(0, i, rate.dateLimit.format(Constants.MMM_DD_YYYY));
                Label labelRateType_i = new Label(1, i, rate.rateType.name());
                Label labelSence_i = new Label(2, i, rate.sence.name());
                Label labelLevelType_i = new Label(3, i, rate.levelype.name());
                Label labelValue_i = new Label(4, i, rate.value + "");
                ws.addCell(labelDateLimit_i);
                ws.addCell(labelRateType_i);
                ws.addCell(labelSence_i);
                ws.addCell(labelLevelType_i);
                ws.addCell(labelValue_i);
            }
            wwb.write();
            wwb.close();
            return fileName;
        } catch (Exception e) {
            try {
                if (wwb != null) wwb.close();
            } catch (Exception ignore) {
                logger.error("Failed to close workbook, {}", Throwables.getStackTraceAsString(ignore));
            }
            return null;
        }
    }
}
