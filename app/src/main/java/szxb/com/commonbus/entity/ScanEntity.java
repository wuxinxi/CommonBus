package szxb.com.commonbus.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者: Tangren on 2017/7/17
 * 包名：com.szxb.buspay.entity
 * 邮箱：996489865@qq.com
 * TODO:扫码交易表
 */
@Entity
public class ScanEntity {

    @Id(autoincrement = true)
    private Long id;
    private String open_id;
    private String mch_trx_id;
    private Long order_time;
    private int total_fee;
    private int pay_fee;
    private String city_code;
    private int in_station_id; //上车站台编号
    private String in_station_name; //站台名称
    private int paystatus; //支付状态
    private String transaction_id;//result=0时返回财付通单号
    private String status;
    private String status_desc;
    private String record;
    @Generated(hash = 821945678)
    public ScanEntity(Long id, String open_id, String mch_trx_id, Long order_time,
            int total_fee, int pay_fee, String city_code, int in_station_id,
            String in_station_name, int paystatus, String transaction_id,
            String status, String status_desc, String record) {
        this.id = id;
        this.open_id = open_id;
        this.mch_trx_id = mch_trx_id;
        this.order_time = order_time;
        this.total_fee = total_fee;
        this.pay_fee = pay_fee;
        this.city_code = city_code;
        this.in_station_id = in_station_id;
        this.in_station_name = in_station_name;
        this.paystatus = paystatus;
        this.transaction_id = transaction_id;
        this.status = status;
        this.status_desc = status_desc;
        this.record = record;
    }
    @Generated(hash = 259370961)
    public ScanEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOpen_id() {
        return this.open_id;
    }
    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }
    public String getMch_trx_id() {
        return this.mch_trx_id;
    }
    public void setMch_trx_id(String mch_trx_id) {
        this.mch_trx_id = mch_trx_id;
    }
    public Long getOrder_time() {
        return this.order_time;
    }
    public void setOrder_time(Long order_time) {
        this.order_time = order_time;
    }
    public int getTotal_fee() {
        return this.total_fee;
    }
    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }
    public int getPay_fee() {
        return this.pay_fee;
    }
    public void setPay_fee(int pay_fee) {
        this.pay_fee = pay_fee;
    }
    public String getCity_code() {
        return this.city_code;
    }
    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }
    public int getIn_station_id() {
        return this.in_station_id;
    }
    public void setIn_station_id(int in_station_id) {
        this.in_station_id = in_station_id;
    }
    public String getIn_station_name() {
        return this.in_station_name;
    }
    public void setIn_station_name(String in_station_name) {
        this.in_station_name = in_station_name;
    }
    public int getPaystatus() {
        return this.paystatus;
    }
    public void setPaystatus(int paystatus) {
        this.paystatus = paystatus;
    }
    public String getTransaction_id() {
        return this.transaction_id;
    }
    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus_desc() {
        return this.status_desc;
    }
    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }
    public String getRecord() {
        return this.record;
    }
    public void setRecord(String record) {
        this.record = record;
    }
}
