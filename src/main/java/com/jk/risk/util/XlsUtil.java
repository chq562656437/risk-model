package com.jk.risk.util;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jk.risk.common.domain.Assets;
import com.jk.risk.common.domain.InterestRate;
import com.jk.risk.common.domain.InterestRatePreView;
import com.jk.risk.common.domain.PressureParam;
import com.jk.risk.common.domain.enumeration.IEnum;
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
import java.util.Map;

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
     * @return 输入参数map
     * @throws Exception error
     */
    public static Map<IEnum.InputSheet, Object> formatInput(java.io.File file) throws Exception{
        try (FileInputStream in = new FileInputStream(file)){
            org.apache.poi.ss.usermodel.Workbook wk = StreamingReader.builder()
                    .rowCacheSize(100)      //缓存到内存中的行数，默认是10
                    .bufferSize(4096)       //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(in);              //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            Map<IEnum.InputSheet, Object> res = Maps.newConcurrentMap();
            for (Sheet sheet : wk) {
                String sheetName = sheet.getSheetName();
                IEnum.InputSheet inputSheet = IEnum.InputSheet.valueOf(sheetName);
                switch (inputSheet) {
                    case InputAssets:
                        res.put(IEnum.InputSheet.InputAssets, importAssets(sheet));
                        break;
                    case InputInterestRate:
                        res.put(IEnum.InputSheet.InputInterestRate, importInterestRates(sheet));
                        break;
                    case InputPressureParam:
                        res.put(IEnum.InputSheet.InputPressureParam, importPressureParam(sheet));
                        break;
                    default:
                        break;
                }
            }
            return res;
        }catch (Exception e){
            throw new RiskSystemException("生成[List<InterestRate>]错误, 请检查数据是否正确!");
        }
    }


    public static List<Assets> importAssets(Sheet sheet) throws Exception{
        List<Assets> assetsList = Lists.newArrayList();
        int rowNum = sheet.getLastRowNum();
        if(rowNum < 1) return Lists.newArrayList();
        for (int i = 1; i <= rowNum; i++){
            Assets assets = new Assets();
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            if(cell != null){
                String aRiskName = cell.getStringCellValue();
                assets.aRiskType = IEnum.ARiskType.of(aRiskName);
            }

            cell = row.getCell(1);
            if(cell !=null) {
                String bRiskName = cell.getStringCellValue();
                assets.bRiskType = IEnum.BRiskType.of(bRiskName);
            }

            cell = row.getCell(2);
            if(cell !=null) {
                String aAssetsName = cell.getStringCellValue();
                assets.aAssetsType = IEnum.AAssetsType.of(aAssetsName);
            }

            cell = row.getCell(3);
            if(cell !=null) {
                String bAssetsName = cell.getStringCellValue();
                assets.bAssetsType = IEnum.BAssetsType.of(bAssetsName);
            }

            cell = row.getCell(4);
            if(cell !=null) {
                String cAssetsName = cell.getStringCellValue();
                assets.cAssetsType = IEnum.CAssetsType.of(cAssetsName);
            }

            cell = row.getCell(5);
            assets.assetsID = cell.getStringCellValue();

            cell = row.getCell(6);
            assets.assetsCode = cell.getStringCellValue();

            cell = row.getCell(7);
            assets.assetsName = cell.getStringCellValue();

            cell = row.getCell(8);
            assets.marketCode = cell.getStringCellValue();

            cell = row.getCell(9);
            if(cell !=null) {
                String assetsName = cell.getStringCellValue();
                assets.assetsType = IEnum.AssetsType.of(assetsName);
            }

            cell = row.getCell(10);
            if(cell !=null) {
                String accountName = cell.getStringCellValue();
                assets.accountType = IEnum.AccountType.of(accountName);
            }

            cell = row.getCell(11);
            if(cell !=null) {
                String pricingWayName = cell.getStringCellValue();
                assets.pricingWay = IEnum.PricingWay.of(pricingWayName);
            }

            cell = row.getCell(12);
            if(cell != null) assets.clzOwn = cell.getStringCellValue();

            cell = row.getCell(13);
            if(cell != null) assets.assetsClz = cell.getStringCellValue();

            cell = row.getCell(14);
            if(cell != null) assets.couponRate = cell.getNumericCellValue();

            cell = row.getCell(15);
            if(cell != null) assets.payInterestFrequency = (int)cell.getNumericCellValue();

            cell = row.getCell(16);
            if(cell != null) assets.issueDate = cell.getDateCellValue();

            cell = row.getCell(17);
            if(cell != null) assets.value = cell.getNumericCellValue();

            cell = row.getCell(18);
            if(cell != null) assets.buyDate = cell.getDateCellValue();

            cell = row.getCell(19);
            if(cell != null) {
                String creditLevelName = cell.getStringCellValue();
                assets.creditLevel = IEnum.CreditLevel.valueOf(creditLevelName);
            }

            cell = row.getCell(20);
            if(cell != null) assets.moneyType = cell.getStringCellValue();

            cell = row.getCell(21);
            if(cell != null) assets.bookValue = cell.getNumericCellValue();

            cell = row.getCell(22);
            if(cell != null) assets.marketValue = cell.getNumericCellValue();

            cell = row.getCell(23);
            if(cell != null) assets.buyNum = cell.getNumericCellValue();

            cell = row.getCell(24);
            if(cell != null) assets.purchaseCost = cell.getNumericCellValue();

            cell = row.getCell(25);
            if(cell != null) assets.bondDenomination = cell.getNumericCellValue();

            cell = row.getCell(26);
            if(cell != null) assets.valid = (int)cell.getNumericCellValue();

            cell = row.getCell(27);
            if(cell != null) assets.amendDuration = cell.getNumericCellValue();

            cell = row.getCell(28);
            if(cell != null) assets.rightDate = cell.getDateCellValue();

            cell = row.getCell(29);
            if(cell != null) {
                int rightType = (int)cell.getNumericCellValue();
                assets.rightType = IEnum.RightType.of(rightType);
            }

            cell = row.getCell(30);
            if(cell != null) assets.redemptionPrice = cell.getNumericCellValue();

            cell = row.getCell(31);
            if(cell != null) assets.extraCouponRate = cell.getNumericCellValue();

            cell = row.getCell(32);
            if(cell != null) assets.receivableInterest = cell.getNumericCellValue();

            cell = row.getCell(33);
            if(cell != null) assets.fairValChange = cell.getNumericCellValue();

            cell = row.getCell(34);
            if(cell != null) assets.RF0 = cell.getStringCellValue();

            cell = row.getCell(35);
            if(cell != null) assets.K1 = cell.getStringCellValue();

            cell = row.getCell(36);
            if(cell != null) assets.K2 = cell.getStringCellValue();

            cell = row.getCell(37);
            if(cell != null) assets.K3 = cell.getStringCellValue();

            cell = row.getCell(38);
            if(cell != null) assets.recognizedValue = cell.getNumericCellValue();

            cell = row.getCell(39);
            if(cell != null) assets.RF = cell.getStringCellValue();

            cell = row.getCell(40);
            if(cell != null) assets.MC = cell.getNumericCellValue();

            assetsList.add(assets);
        }
        return assetsList;
    }

    public static List<InterestRate> importInterestRates(Sheet sheet) throws Exception{
        List<InterestRate> rates = Lists.newArrayList();
        int rowNum = sheet.getLastRowNum();
        if(rowNum < 1) return Lists.newArrayList();
        for (int i = 2; i <= rowNum; i++){
            InterestRate rate = new InterestRate();
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            rate.yearth = cell.getNumericCellValue();

            cell = row.getCell(1);
            if(cell !=null) rate.basicNationalDebt = cell.getNumericCellValue();

            cell = row.getCell(2);
            if(cell !=null) rate.nationalDebtUp = cell.getNumericCellValue();

            cell = row.getCell(3);
            if(cell !=null) rate.nationalDebtDown = cell.getNumericCellValue();

            cell = row.getCell(4);
            if(cell !=null) rate.AAA = cell.getNumericCellValue();

            cell = row.getCell(5);
            rate.AAPlus = cell.getNumericCellValue();

            cell = row.getCell(6);
            rate.AA = cell.getNumericCellValue();

            cell = row.getCell(7);
            rate.AAMinus = cell.getNumericCellValue();

            cell = row.getCell(8);
            rate.APlus = cell.getNumericCellValue();

            cell = row.getCell(9);
            rate.A = cell.getNumericCellValue();

            cell = row.getCell(10);
            rate.B = cell.getNumericCellValue();

            rates.add(rate);
        }
        return rates;
    }

    public static List<PressureParam> importPressureParam(Sheet sheet) throws Exception{
        List<PressureParam> res = Lists.newArrayList();
        int rowNum = sheet.getLastRowNum();
        if(rowNum < 1) return Lists.newArrayList();
        for (int i = 1; i <= rowNum; i++){
            PressureParam pressureParam = new PressureParam();
            Row row = sheet.getRow(i);

            // 基础情景start
            Cell cell = row.getCell(0);
            pressureParam.term = (int)cell.getNumericCellValue();

            cell = row.getCell(1);
            if(cell !=null) pressureParam.assetslDebtUp = cell.getNumericCellValue();

            cell = row.getCell(2);
            if(cell !=null) pressureParam.assetslDebtDown = cell.getNumericCellValue();
            pressureParam.type = 0;

            res.add(pressureParam);
            // 基础情景end


            // 自设压力情景 start
            cell = row.getCell(3);
            if(cell !=null) pressureParam.assetslDebtUp = cell.getNumericCellValue();

            cell = row.getCell(4);
            if(cell !=null) pressureParam.assetslDebtDown = cell.getNumericCellValue();
            pressureParam.type = 1;
            res.add(pressureParam);
            // 自设压力情景end

        }
        return res;
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
