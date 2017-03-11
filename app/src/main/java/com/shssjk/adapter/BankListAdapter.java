package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.person.Bank;
import com.shssjk.utils.SSUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 我的银行卡
 */
public class BankListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<Bank> mConsignees;
    private Context mContext;
    private BankListListener mAddressListListener;
    public BankListAdapter(Context context, BankListListener addressListListener) {
        this.mContext = context;
        this.mAddressListListener = addressListListener;
    }

    public void setData(List<Bank> consignees) {
        if (consignees == null) return;
        this.mConsignees = consignees;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mConsignees == null) return 0;
        return mConsignees.size();
    }

    @Override
    public Object getItem(int position) {
        if (mConsignees == null) return null;
        return mConsignees.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mConsignees == null) return -1;
        return Long.valueOf(mConsignees.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_bank_list_item, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       //获取该行数据
        Bank bank = (Bank) mConsignees.get(position);
        //设置数据到View
//		holder.setText(bank.getConsignee());
		holder.bankNameTxtv.setText(bank.getBank());
        if(!SSUtils.isEmpty(bank.getCode())){
            holder.bankNumTxtv.setText(SSUtils.repalceAndHideBankNum(bank.getCode()));
        }
        String imgUrl1 = MobileConstants.BASE_HOST+bank.getBank_url();
        Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.bankIconImv);
//        是否是默认银行卡（1 是，0 否）
		if("1".equals(bank.getIs_default())){
			holder.bankSetdefaultBtn.setBackgroundResource(R.drawable.icon_checked);
            holder.bankDefaultTxtv.setText("默认银行卡");
		}else{
			holder.bankSetdefaultBtn.setBackgroundResource(R.drawable.icon_checkno);
            holder.bankDefaultTxtv.setText("设为默认");
		}
        holder.bankDeleteBtn.setOnClickListener(this);
        holder.bankDeleteTxtv.setOnClickListener(this);
        holder.bankDeleteBtn.setTag(position);
        holder.bankDeleteTxtv.setTag(position);

        holder.bankEditBtn.setOnClickListener(this);
        holder.bankEditBtn.setTag(position);

        holder.bankEditTxtv.setOnClickListener(this);
        holder.bankEditTxtv.setTag(position);



        holder.bankSetdefaultBtn.setOnClickListener(this);
        holder.bankSetdefaultBtn.setTag(position);

        holder.bankDefaultTxtv.setOnClickListener(this);
        holder.bankDefaultTxtv.setTag(position);



        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = Integer.valueOf(v.getTag().toString());
        Bank consignee = mConsignees.get(position);
        switch (v.getId()) {
            case R.id.bank_delete_btn:
            case R.id.bank_delete_txtv:
                if (mAddressListListener != null) mAddressListListener.onItemDelete(consignee);
                break;
            case R.id.bank_edit_btn:
            case R.id.bank_edit_txtv:

                if (mAddressListListener != null) mAddressListListener.onItemEdit(consignee);
                break;
            case R.id.address_setdefault_btn:
            case R.id.bank_default_txtv:
                if (mAddressListListener != null) mAddressListListener.onItemSetDefault(consignee);
                break;
        }
    }
    public interface BankListListener {
        public void onItemDelete(Bank consigneeAddress);

        public void onItemEdit(Bank consigneeAddress);

        public void onItemSetDefault(Bank consigneeAddress);
    }
    class ViewHolder {
        @Bind(R.id.bank_icon_imv)
        ImageView bankIconImv;
        @Bind(R.id.bank_name_txtv)
        TextView bankNameTxtv;
        @Bind(R.id.bank_num_txtv)
        TextView bankNumTxtv;
        @Bind(R.id.address_setdefault_btn)
        Button bankSetdefaultBtn;
        @Bind(R.id.bank_default_txtv)
        TextView bankDefaultTxtv;
        @Bind(R.id.bank_delete_txtv)
        TextView bankDeleteTxtv;
        @Bind(R.id.bank_delete_btn)
        Button bankDeleteBtn;
        @Bind(R.id.bank_edit_txtv)
        TextView bankEditTxtv;
        @Bind(R.id.bank_edit_btn)
        Button bankEditBtn;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
