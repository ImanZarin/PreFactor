package com.skywavestudios.prefactor;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;

/**
 * Created by Iman on 4/10/2017.
 */

public class FillProduct_Fragment extends DialogFragment {

    EditText no;
    AutoCompleteTextView unit;
    EditText fee;
    TextView total_cost;
    AutoCompleteTextView name;
    Button ok;
    Button delete;
    ArrayList<String> product_list = new ArrayList<String>();
    ArrayList<String> unit_list = new ArrayList<String>();
    SharedPreferences sp2;
    SharedPreferences.Editor e2;
    public int RowNO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fillproduct, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initilize(view);
        fee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    _Total_Change_Listener();

                } catch (Exception e) {

                }
            }
        });
        no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".") && s.subSequence(s.toString().lastIndexOf("."), s.length()).length() > 2) {
                    if (s.subSequence(s.toString().lastIndexOf("."), s.length()).length() > 3)
                        no.setText(s.subSequence(0, s.length() - 1));
                    unit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    _Total_Change_Listener();

                } catch (Exception e) {

                }
            }
        });
    }

    public static FillProduct_Fragment newInstance(String title) {
        FillProduct_Fragment frag = new FillProduct_Fragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    private void initilize(View v) {
        no = (EditText) v.findViewById(R.id.fillproduct_no_edittext);
        name = (AutoCompleteTextView) v.findViewById(R.id.fillproduct_name_edittext);
        unit = (AutoCompleteTextView) v.findViewById(R.id.fillproduct_unit_edittext);
        fee = (EditText) v.findViewById(R.id.fillproduct_fee_edittext);
        total_cost = (TextView) v.findViewById(R.id.fillproduct_totalcost_textview);
        ok = (Button) v.findViewById(R.id.fillproduct_ok);
        delete = (Button) v.findViewById(R.id.fillproduct_delete);
        if (!((MainActivity) getActivity()).mTable[RowNO][1].getText().equals(""))
            name.setText(((MainActivity) getActivity()).mTable[RowNO][1].getText().toString());
        if (!((MainActivity) getActivity()).mTable[RowNO][2].getText().equals(""))
            no.setText(((MainActivity) getActivity()).mTable[RowNO][2].getText().toString());
        if (!((MainActivity) getActivity()).mTable[RowNO][3].getText().equals(""))
            unit.setText(((MainActivity) getActivity()).mTable[RowNO][3].getText().toString());
        fee.setText("");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_product(v);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        sp2 = getContext().getSharedPreferences("", 0);
        e2 = sp2.edit();
        if (sp2.getStringSet(SharedPrefsMainKey.ProductList.toString(), null) != null) {
            product_list = new ArrayList<String>(sp2.getStringSet(SharedPrefsMainKey.ProductList.toString(), null));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, product_list);
            name.setAdapter(adapter);
        }
        if (sp2.getStringSet(SharedPrefsMainKey.UnitList.toString(), null) != null) {
            unit_list = new ArrayList<String>(sp2.getStringSet(SharedPrefsMainKey.UnitList.toString(), null));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, unit_list);
            unit.setAdapter(adapter);
        }

    }

    private void delete() {
        Product new_product = new Product();
        new_product.Name = "";
        new_product.Scale = "";
        new_product.Fee = 0;
        new_product.No = 0;
        ((MainActivity) getActivity()).edit_product(new_product, RowNO);
        this.dismiss();
    }

    public void save_product(View v) {
        save_product_name();
        Product new_product = new Product();
        new_product.Name = name.getText().toString();
        new_product.Scale = unit.getText().toString();
        try {
            new_product.Fee = Integer.valueOf(fee.getText().toString());
            new_product.No = Float.parseFloat(no.getText().toString());
            ((MainActivity) getActivity()).edit_product(new_product, RowNO);
            this.dismiss();
        } catch (Exception e) {
            Toast t = Toast.makeText(getContext(), R.string.fillproduct_errormessage, Toast.LENGTH_LONG);
            t.show();
            e.printStackTrace();
        }
    }

    private void save_product_name() {
        Set<String> pl = new HashSet<String>();
        if (!product_list.contains(name.getText()))
            product_list.add(name.getText().toString());
        pl.addAll(product_list);
        Set<String> ul = new HashSet<String>();
        if (!unit_list.contains(unit.getText()))
            unit_list.add(unit.getText().toString());
        ul.addAll(unit_list);
        e2.putStringSet(SharedPrefsMainKey.ProductList.toString(), pl);
        e2.putStringSet(SharedPrefsMainKey.UnitList.toString(), ul);
        e2.commit();
    }

    private void _Total_Change_Listener() {
        String _totalcost = getString(R.string.fillproduct_totalcost) + "  ";
        _totalcost += AppConstant.Int_To_Price(Integer.valueOf(no.getText().toString()) * Integer.valueOf(fee.getText().toString()));
        total_cost.setText(_totalcost);
    }
}
