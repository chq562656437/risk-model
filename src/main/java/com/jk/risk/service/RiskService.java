package com.jk.risk.service;import com.jk.risk.common.domain.InterestRate;import com.jk.risk.common.domain.InterestRatePreView;import com.jk.risk.common.domain.enumeration.IEnum;import com.jk.risk.util.XlsUtil;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.stereotype.Service;import java.util.List;import java.util.Map;import java.util.concurrent.ExecutorService;import java.util.concurrent.Executors;/** * 业务计算 * User: allen * Date: 2017/12/1 下午2:02 */@Servicepublic class RiskService {    private final Logger          logger  = LoggerFactory.getLogger(getClass());    private final ExecutorService service = Executors.newFixedThreadPool(20);    /**     * 导入文件     * @param file Excel文件     * @return 利率list     * @throws Exception error     */    public String importInterestRatesFromFile(java.io.File file) throws Exception{        Map<IEnum.InputSheet, Object> input = XlsUtil.formatInput(file);        // caculate()        // preView caculate        // TODO 业务处理        return XlsUtil.exportRateAndDiscountFactor(null);    }    public String caculate(){        return null;    }    /**     * 计算贴现因子     * input1: 即期利率     * input2: 压力参数     */    public List<InterestRatePreView> caculateDiscountFactor(List<InterestRate> rates/*,压力参数*/){        return null;    }    /**     * 资产价值计算     * input1: 资产基础信息     * input2: 资产现金流     *///    public List<InterestRatePreView> caculateZichanValue(List<InterestRate> rates, List<AssetCashFlow> cashFlows){//        return null;//    }}