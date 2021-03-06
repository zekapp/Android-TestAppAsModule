package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.model.MCartItemInfo;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.presenters.CardSelector;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.StringUtils;

/**
 * Created by Zeki Guler on 28/07/15.
 */
public class StockDetailType2Fragment extends BaseFragment {

    public  static final String TAG            = StockDetailType1Fragment.class.getSimpleName();
    public  static final String STOCK_ITEM_KEY = "STOCK_ITEM_KEY_TYPE_2";


    /*@Bind(R.id.stock_detail_name)    */   TextView  mStockName;
    /*@Bind(R.id.stock_detail_discount)*/   TextView  mStockDiscount;
    /*@Bind(R.id.stock_detail_img)     */   ImageView mStockImage;
    /*@Bind(R.id.stock_detail_def)     */   TextView  mStockDef;
/*//    @Bind(R.id.digital_option)     */     TableRow  mPhysicalOptTr; // hide if digital selected.
    /*@Bind(R.id.delivery_type)        */   TextView  mDeliveryType;
    /*@Bind(R.id.card_price)           */   TextView  mCardPrice;
    /*@Bind(R.id.iten_quantity)        */   TextView  mQuantity;
    /*@Bind(R.id.stock_terms)          */   TextView  mTerms;
    /*@Bind(R.id.terms_container)      */   LinearLayout mTermsLayout;
    /*@Bind(R.id.card_value_title)     */   TextView  mCardValueName;
/**/
    /***/
     /** Karl added*/
     /**/
    /*@Bind(R.id.card_option_title)    */   TextView mCardOptionTitle;
    /*@Bind(R.id.bullet1)              */   TextView mBulletText1;
    /*@Bind(R.id.bullet2)              */   TextView mBulletText2;
    /*@Bind(R.id.bullet3)              */   TextView mBulletText3;
    /*@Bind(R.id.bullet4)              */   TextView mBulletText4;


    private MStockItem mStockItem;
    private CardSelector mCardSelector;

    public static StockDetailType2Fragment newInstance(MStockItem stockItem) {
        StockDetailType2Fragment fragment = new StockDetailType2Fragment();
        Bundle bundle   = new Bundle();

        bundle.putSerializable(STOCK_ITEM_KEY, stockItem);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mStockItem = (MStockItem)getArguments().getSerializable(STOCK_ITEM_KEY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mCardSelector == null)
            mCardSelector = new CardSelector(getActivity(), mStockItem, new CustomCrdSelecterInterface());

        updateViews();
    }

    private void updateViews() {
        mStockName    .setText(mStockItem.getName());
        mStockDef     .setText(Html.fromHtml(mStockItem.getDescriptionLong()));

        mStockDiscount.setVisibility(View.VISIBLE);

        if(mStockItem.getDiscountType() == Constants.DiscountType.TEXT){
            mStockDiscount.setText(mStockItem.getDiscountText());
        } else if (mStockItem.getDiscountType() == Constants.DiscountType.TOTAL){
            mStockDiscount.setText("$" + mStockItem.getDiscount());
        } else if (mStockItem.getDiscountType() == Constants.DiscountType.PERCENTAGE){
            mStockDiscount.setText(StringUtils.formatDoubleValue(mStockItem.getDiscount()) + "% OFF");
        } else {
            mStockDiscount.setVisibility(mStockItem.getDiscount() > 0 ? View.VISIBLE : View.GONE);
        }

        if (mStockItem.getCategoryPageImage() != null && !mStockItem.getCategoryPageImage().isEmpty()){
            Picasso.with(getActivity())
                    .load(Constants.Url.IMAGE_BASE + mStockItem.getCategoryPageImage())// get valid one
                    .into(mStockImage);
        }

        boolean isTermShown = mStockItem.getTerms() != null && !mStockItem.getTerms().isEmpty();
        mTermsLayout.setVisibility(isTermShown ? View.VISIBLE : View.GONE);
        if(isTermShown) {
            /**
             * Karl added, text view does not support <li><ul> tag in html
             * delete <ul></ul>, replace <li> with  &#8226; </li> with <br/>
             */
            String s = mStockItem.getTerms().replace("<ul>","");
            s = s.replace("</ul>","");
            s = s.replace("<li>","&#8226; ");
            s = s.replace("</li>","<br/><br/>");

            mTerms.setText(Html.fromHtml(s));
        }
        mCardValueName.setText(mStockItem.isDisplayNameAsCardType() ? "CARD TYPE:" : "CARD VALUE:");
    }

    @Override
    protected String getTitle() {
        return mStockItem.getName();
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_stock_detail_type2;
    }


    /*@OnClick(R.id.delivery_type)*/
    void deliveryTypeClicked(){
        mCardSelector.deliveryTypeClicked();
    }

    /*@OnClick(R.id.card_price)*/
    void cardPriceClicked(){
        mCardSelector.priceClicked();
    }

    /*@OnClick(R.id.stock_detail_increase_item)*/
    void itemIncreased(){
        mCardSelector.increaseQuantityByOne();
    }

    /*@OnClick(R.id.stock_detail_decrease_item)*/
    void itemDecreased(){
        mCardSelector.decreaseQuantityByOne();
    }

    /*@OnClick(R.id.stock_detail_add_to_card)*/
    void addToCart(){
        mCardSelector.addToCart();
    }

    private class CustomCrdSelecterInterface implements CardSelector.ICardSelectorResponse {
        @Override
        public void itemSelected(MCartItemInfo selectedCard, int deliveryType, String price) {
            mDeliveryType.setText(getResources().getStringArray(R.array.card_delivery_type)[deliveryType]);
            mCardPrice.setText(String.valueOf(price));

//            if(deliveryType == Constants.FulfilmentType.DIGITAL)
//                mPhysicalOptTr.setVisibility(View.GONE);
//            else
//                mPhysicalOptTr.setVisibility(View.VISIBLE);

            setDeliveryTypeInstruction(deliveryType);
        }

        @Override
        public void cardAddedToStore(MShoppingCartVM shoppingCartOverall) {
            mIActivity.saveCartToDb(shoppingCartOverall);
            Toast.makeText(getActivity(), "Item added to your cart.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void quantityChanged(int quantity) {
            mQuantity.setText("QUANTITY: " + String.valueOf(quantity));
        }
    }

    /**
     * Karl added
     */
    private void setDeliveryTypeInstruction(int deliveryType){
        if(deliveryType == Constants.FulfilmentType.DIGITAL){
            mCardOptionTitle.setText(getResources().getString(R.string.card_options_digital));
            mBulletText1.setText(getResources().getString(R.string.options_bullet_digital_1));
            mBulletText2.setText(getResources().getString(R.string.options_bullet_digital_2));
            mBulletText3.setText(getResources().getString(R.string.options_bullet_digital_3));
            mBulletText4.setText(getResources().getString(R.string.options_bullet_digital_4));
        } else {
            mCardOptionTitle.setText(getResources().getString(R.string.card_options_physical));
            mBulletText1.setText(getResources().getString(R.string.options_bullet_physical_1));
            mBulletText2.setText(getResources().getString(R.string.options_bullet_physical_2));
            mBulletText3.setText(getResources().getString(R.string.options_bullet_physical_3));
            mBulletText4.setText(getResources().getString(R.string.options_bullet_physical_4));
        }
    }
}
