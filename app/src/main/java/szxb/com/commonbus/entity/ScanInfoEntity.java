package szxb.com.commonbus.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者: Tangren on 2017/7/19
 * 包名：com.szxb.buspay.entity
 * 邮箱：996489865@qq.com
 * TODO:扫码记录表
 * status:支付状态，默认false
 * biz_data_single:单个扫码记录的jsonObject
 */
@Entity
public class ScanInfoEntity {

    @Id(autoincrement = true)
    private Long id;

    private boolean status = false;

    private String biz_data_single;

    private String mch_trx_id;

    public String getMch_trx_id() {
        return mch_trx_id;
    }

    public void setMch_trx_id(String mch_trx_id) {
        this.mch_trx_id = mch_trx_id;
    }

    //保存的时间yyyy-MM-dd HH:mm:ss
    private String time;

    @Generated(hash = 1844689823)
    public ScanInfoEntity(Long id, boolean status, String biz_data_single,
            String mch_trx_id, String time) {
        this.id = id;
        this.status = status;
        this.biz_data_single = biz_data_single;
        this.mch_trx_id = mch_trx_id;
        this.time = time;
    }

    @Generated(hash = 1829284389)
    public ScanInfoEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBiz_data_single() {
        return this.biz_data_single;
    }

    public void setBiz_data_single(String biz_data_single) {
        this.biz_data_single = biz_data_single;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ScanInfoEntity{" +
                "id=" + id +
                ", status=" + status +
                ", biz_data_single='" + biz_data_single + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
