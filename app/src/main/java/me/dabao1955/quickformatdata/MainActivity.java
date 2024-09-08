package me.dabao1955.quickformatdata;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity { 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("要留清白在人间");
        builder.setMessage("请选择 data 分区格式以进行格式化 data 分区操作");

        builder.setPositiveButton("ext4", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result1 = executeCommand("su -c /system/bin/mke2fs -t ext4 /dev/block/by-name/userdata");
				Toast.makeText(MainActivity.this, "手机将在5秒钟后重启！", Toast.LENGTH_SHORT).show();
				String result2 = executeCommand("sleep 5s && su -c reboot"); 

                /*AlertDialog.Builder resultDialog = new AlertDialog.Builder(MainActivity.this);
                resultDialog.setTitle("命令执行结果");
                resultDialog.setMessage(result1);

                resultDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                resultDialog.show();*/
            }
        });
	builder.setNegativeButton("f2fs", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String result1 = executeCommand("su -c /system/bin/mke2fs -t f2fs /dev/block/by-name/userdata");
			Toast.makeText(MainActivity.this, "手机将在5秒钟后重启！", Toast.LENGTH_SHORT).show();
			String result2 = executeCommand("sleep 5s && su -c reboot"); 
			dialog.dismiss();
			}
		});
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            process.waitFor();
        } catch (Exception e) {
            output.append("命令执行失败: ").append(e.getMessage());
        }
        return output.toString();
    }
}
