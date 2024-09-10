package me.dabao1955.quickformatdata;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.text.util.Linkify;
import android.app.AlertDialog;
import android.text.method.LinkMovementMethod;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isDeviceRooted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("要留清白在人间");
            builder.setMessage("请问你真的要留住清白吗？\n警告：请确保手机里没有重要数据！");

            builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String result1 = executeCommand("su -c rm -rf $(find /data/)");
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
    	    builder.setNegativeButton("我偏不", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			Toast.makeText(MainActivity.this, "清白再见👋🏻", Toast.LENGTH_SHORT).show();
    			dialog.dismiss();
	    		}
    		});
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                    Toast.makeText(MainActivity.this, "手机未root，无法使用此软件！", Toast.LENGTH_LONG).show();
            }
		}

            public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_main, menu);
                return true;
            }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "别点了，没什么用的，只是用来测试而已", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("关于");
				final TextView aboutTextView = new TextView(this);
                aboutTextView.setText("作者：dabao1955\n开源地址：https://github.com/SekaiMoe/qingbai");
				aboutTextView.setAutoLinkMask(Linkify.WEB_URLS);
				aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
				aboutTextView.setPadding(50, 50, 50, 50);
				builder.setView(aboutTextView);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            @Override
		    		public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
			        }
				});
				builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isDeviceRooted() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su -c id");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("uid=0")) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
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
