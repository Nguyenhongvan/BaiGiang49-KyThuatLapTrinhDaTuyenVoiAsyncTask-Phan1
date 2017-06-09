package com.vansuzy.baigiang49_kythuatlaptrinhdatuyenvoiasynctask_phan1;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText txtSoButton;
    Button btnVe;
    ProgressBar progressBarPercent;
    LinearLayout layoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyVeButtonThoiGianThuc();
            }
        });
    }

    private void xuLyVeButtonThoiGianThuc() {
        int n = Integer.parseInt(txtSoButton.getText().toString()); // số button cần vẽ
        ButtonTask task = new ButtonTask();
        task.execute(n);    // kích hoạt đa tiến trình (bắt đầu chạy)
    }

    // đối số 1 là input (số button cần vẽ: kiểu Integer), đối số thứ 2 là số phần trăm (0-100), đối số thứ 3 là kiểu trả về
    // đối số 1 đi với doInBackground(), đối số 2 đi với onProgressUpdate(), đối số 3 đi với onPostExecute()
    class ButtonTask extends AsyncTask<Integer, Integer, Void> {

        // 3
        // Hàm này bắt buộc phải được thực thi
        // đối số truyền vào chính là một parameter list (là một mảng một chiều linh động: chúng ta có thể truyền bao nhiêu đối số cũng được)
        // Bất kỳ giá trị nào trong doInBackground thì nó không được đưa lên giao diện mà nó phải đẩy qua hàm onProgressUpdate()
        @Override
        protected Void doInBackground(Integer... params) {
            int n = params[0];
            Random random = new Random();
            for (int i = 0; i < n; i++)
            {
                int percent = i*100/n;
                int value = random.nextInt(500);
                publishProgress(percent, value);  // đẩy giá trị trong doInBackground() qua hàm onProgressUpdate() để hàm onProgressUpdate() có thể vẽ lên giao diện, tức là khi gọi publishProgress() thì lập tức hàm onProgressUpdate() được thực hiện, còn nếu trong doInBackground() chúng ta không gọi publishProgress() thì không bao giờ hàm onProgressUpdate() được làm
                SystemClock.sleep(100);
            }
            return null;
        }

        // 1
        // tự động làm
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layoutButton.removeAllViews();  // xóa tất cả các control bên trong
            progressBarPercent.setProgress(0);
        }

        // 2
        // tự động được thực hiện khi tiến trình kết thúc
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBarPercent.setProgress(100);
        }

        // 4
        // Lấy dữ liệu từ doInBackground() gửi về và hiển thị dữ liệu đó lên giao diện
        // Trong publishProgress() chúng ta truyền thứ tự như thế nào thì trong onProgressUpdate() chúng ta phải lấy đúng thứ tự đó
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int percent = values[0];
            int value = values[1];
            progressBarPercent.setProgress(percent);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            // vẽ 1 button lên giao diện (vẽ theo thời gian thực)
            Button btn = new Button(MainActivity.this);
            btn.setLayoutParams(params);
            btn.setText((value + ""));

            layoutButton.addView(btn);
        }
    }
    private void addControls() {
        txtSoButton = (EditText) findViewById(R.id.txtSoButton);
        btnVe = (Button) findViewById(R.id.btnVeButton);
        progressBarPercent = (ProgressBar) findViewById(R.id.progressBarPercent);
        layoutButton= (LinearLayout) findViewById(R.id.layoutButton);
    }
}
