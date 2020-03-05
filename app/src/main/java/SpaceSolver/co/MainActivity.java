package SpaceSolver.co;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;

public class MainActivity extends AppCompatActivity
{
    // バーコードの各種設定
    String targetData = "";       //バーコードに変換する対象データ
    int    width      = 400;      //作成するバーコードの幅
    int    height     = 200;      //作成するバーコードの高さ

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

            // バーコードフォーマット種別を選択

            // バーコード表示処理
            pix = DisplayBarcode();
            DisplayBitmapImage(pix);

            // バーコード表示を表示状態にする。
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            Log.e("ERROR","例外発生");
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
            Log.e("ERROR","例外発生");
        }
    }


    public int[] DisplayBarcode()
    {
        // CODABAR規格用のデータ変換クラスをインスタンス化する
        CodaBarWriter writer = new CodaBarWriter();
        int[] pixels = null;

        try {
            // 対象データを変換する
            BitMatrix bitMatrix = writer.encode(targetData, BarcodeFormat.CODABAR, width, height);

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
            Log.e("ERROR","例外発生");
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
            Log.e("ERROR","例外発生");
        }
    }
}
