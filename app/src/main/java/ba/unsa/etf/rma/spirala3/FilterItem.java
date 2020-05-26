package ba.unsa.etf.rma.spirala3;

public class FilterItem {
    private String FilterItem;
    private int img;

    public FilterItem(String FilterItem, int img) {
        this.FilterItem = FilterItem;
        this.img = img;
    }

    public String getFilterItem() {
        return FilterItem;
    }

    public int getImg() {
        return img;
    }

    @Override
    public String toString() {
        return  FilterItem;

    }
}
