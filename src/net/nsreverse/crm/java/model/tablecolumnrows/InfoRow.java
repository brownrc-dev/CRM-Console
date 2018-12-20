package net.nsreverse.crm.java.model.tablecolumnrows;

public class InfoRow {
    private String mkey;
    private String mvalue;

    public InfoRow(String key, String value) {
        this.mkey = key;
        this.mvalue = value;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String getMvalue() {
        return mvalue;
    }

    public void setMvalue(String mvalue) {
        this.mvalue = mvalue;
    }
}