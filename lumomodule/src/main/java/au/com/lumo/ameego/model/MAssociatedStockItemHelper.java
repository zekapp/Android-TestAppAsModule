package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 27/07/15.
 */
public class MAssociatedStockItemHelper implements Serializable{
    private MMerchant      merchant;
    private MStockItem     associatedStockItem;
    private boolean        success;             // Indicates True when the operation was successfully completed or false otherwise
    private MErrorHelper   errors;              // The list of errors generated by the APUI Call

    public MStockItem getAssociatedStockItem() {
        return associatedStockItem;
    }

    public void setAssociatedStockItem(MStockItem associatedStockItem) {
        this.associatedStockItem = associatedStockItem;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MErrorHelper getErrors() {
        return errors;
    }

    public void setErrors(MErrorHelper errors) {
        this.errors = errors;
    }

    public MMerchant getMerchant() {
        return merchant;
    }
}
