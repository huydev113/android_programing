package com.example.bai14;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView selection;
    //Khai báo 2 CompleteTextView
    AutoCompleteTextView singleComplete;
    MultiAutoCompleteTextView multiComplete;
    //Khởi tạo mảng tạm để Test
    String arr[]={"hà nội", "Huê", "Sài gòn",
            "hà giang", "Hội an", "Kiên giang", "Lâm đồng", "Long khánh"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            selection = (TextView) findViewById(R.id.selection);
//lấy đối tượng AutoCompleteTextView ra
            singleComplete=(AutoCompleteTextView) findViewById(R.id.editauto);
            ArrayAdapter myadapter = new ArrayAdapter<String>
                    (
                            this,
                            android.R.layout.simple_list_item_1,
                            arr
                    );
//Thiết lập ArrayAdapter
            singleComplete.setAdapter (myadapter);
// Lầy đối tượng MultiAutoCompleteTextView ra
            multiComplete=(MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView1);
//Thiết lập ArrayAdapter
            multiComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr));
//Đối với MultiAutoCompleteTextView bắt buộc phải gọi dòng lệnh này multiComplete.setTokenizer (new MultiAutoCompleteTextView .CommaTokenizer());
            singleComplete.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selection.setText(singleComplete.getText());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
}
}