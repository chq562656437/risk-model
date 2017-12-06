package com.jk.risk.util;

import com.google.common.base.Throwables;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
/**
 * 工具类
 * User: allen
 * Date: 2017/11/29 下午10:30
 */
public class WriteXlsUtil {

    private static final Logger logger    = LoggerFactory.getLogger(WriteXlsUtil.class);
    private static final String FILE_NAME = "/export/Data/RM/test-{date}.xls";

    public static String createAttachment(Map<String, Long> map) {
        String fileName = FILE_NAME.replace("{date}", LocalDate.now(ZoneOffset.ofHours(8)).toString().replace("-", ""));
        WritableWorkbook wwb = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    logger.error("Failed to create warning mail attachment to path {}.", fileName);
                    return null;
                }
            } else {
                return fileName;
            }
            wwb = Workbook.createWorkbook(file);
            WritableSheet ws = wwb.createSheet("test_sheet", 0);
            Label phone = new Label(0, 0, "Column1");
            Label couponId = new Label(1, 0, "Column2");
            ws.addCell(phone);
            ws.addCell(couponId);
            int i = 1;
            for(Map.Entry<String, Long> entry : map.entrySet()){
                String ph = entry.getKey();
                Long value = entry.getValue();
                Label labelKey_i = new Label(0, i, ph);
                Label labelVal_i = new Label(1, i, value + "");
                ws.addCell(labelKey_i);
                ws.addCell(labelVal_i);
                i ++;
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
