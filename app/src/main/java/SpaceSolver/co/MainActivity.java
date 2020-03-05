package SpaceSolver.co;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.ITFWriter;

public class MainActivity extends AppCompatActivity
{
    // バーコードの各種設定
    String targetData = "";       //バーコードに変換する対象データ
    int    width      = 400;      //作成するバーコードの幅
    int    height     = 200;      //作成するバーコードの高さ

    // 選択肢
    private String[] spinnerItems = {"CODABAR", "CODE_128", "ITF"};
    public TextView SelectItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.SelectFormat);

        // ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        spinner.setAdapter(adapter);

        try
        {
            // リスナーを登録
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                //　アイテムが選択された時
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    Spinner spinner = (Spinner) parent;
                    String item = (String) spinner.getSelectedItem();
                }

                //　アイテムが選択されなかった
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });
        }
        catch (Exception e)
        {
            Log.e("ERROR","例外発生" + e);
        }
    }

    public void OnClickAddButton(View view)
    {
        ImageView imageView = findViewById(R.id.imageView);
        EditText editText = findViewById(R.id.inputNumber);
        TextView textView = findViewById(R.id.NumberDisplayView);
        int[] pix;

        try {
            // 入力データをバーコードに変換する対象データに代入
            targetData = editText.getText().toString();

            // バーコード表示処理
            pix = DisplayBarcode();
            DisplayBitmapImage(pix);

            // バーコード表示を表示状態にする。
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            Log.e("ERROR","例外発生" + e);
        }
    }

    public void OnClickClearButton(View view) {
        ImageView imageView = findViewById(R.id.imageView);
        EditText editText = findViewById(R.id.inputNumber);
        TextView textView = findViewById(R.id.NumberDisplayView);

        try {
            // 入力データを消去
            editText.setText("");

            // バーコード表示を非表示状態にする。
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
        catch (Exception e)
        {
            Log.e("ERROR","例外発生" + e);
        }
    }

    public int[] DisplayBarcode()
    {
        int[] pixels = null;
        Spinner spinner = findViewById(R.id.SelectFormat);

        try
        {
            BitMatrix bitMatrix = null;

            if("CODABAR" == (String) spinner.getSelectedItem())
            {
                CodaBarWriter writer = new CodaBarWriter();
                bitMatrix = writer.encode(targetData, BarcodeFormat.CODABAR, width, height);
            }
            else if("CODE_128" == (String) spinner.getSelectedItem())
            {
                Code128Writer writer = new Code128Writer();
                bitMatrix = writer.encode(targetData, BarcodeFormat.CODE_128, width, height);
            }
            else if("TIF" == (String) spinner.getSelectedItem())
            {
                ITFWriter writer = new ITFWriter();
                bitMatrix = writer.encode(targetData, BarcodeFormat.ITF, width, height);
            }
            else
            {
                CodaBarWriter writer = new CodaBarWriter();
                bitMatrix = writer.encode(targetData, BarcodeFormat.CODABAR, width, height);
            }
            
            // BitMatrixのデータが「true」の時は「黒」を設定し、「false」の時は「白」を設定する
            pixels = new int[width * height];
            for (int y = 0; y < height; y++)
            {
                int offset = y * width;
                for (int x = 0; x < width; x++)
                {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
        }
        catch (WriterException e)
        {
            Log.e("ERROR","例外発生" + e);
        }
        return pixels;
    }

    public void DisplayBitmapImage(int[] pixels)
    {
        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.NumberDisplayView);

        try {
            // ビットマップ形式に変換する
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            // イメージビューに表示する
            imageView.setImageBitmap(bitmap);

            // 取得したテキストを TextView に張り付ける
            textView.setText(targetData);
        }
        catch (Exception e)
        {
            Log.e("ERROR","例外発生" + e);
        }
    }
}
