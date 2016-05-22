package mgit.notification_timer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button start;
    private ProgressDialog progressDialog;
    private MessageHandler messageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("CountDown");
        progressDialog.setMessage("Plz Wait");
        progressDialog.setCancelable(true);
        messageHandler = new MessageHandler();
        start = (Button) findViewById(R.id.counterStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Thread t = new Thread(new Timer());
                t.start();
            }
        });
    }
    public void doNotify(){
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent intent=PendingIntent.getActivity(this,100,new Intent(this,Main2Activity.class),0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this);
        builder.setContentTitle("Timed Notification");
        builder.setContentText("Its a Timer Notification");//it will be displayed just below Title
        builder.setSound(sound);
        builder.setSmallIcon(R.drawable.icon);//icon will be shown left side in notification
        builder.setTicker("its ticker");//this will appear at the top of the screen
        builder.setContentIntent(intent);
        builder.setAutoCancel(true);

        Notification notification=builder.build();
        NotificationManager manager=(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.notify(100,notification);//takes 2 objs 1.id 2.notification obj
    }

    private class Timer implements Runnable {

        @Override
        public void run() {
            for (int i = 4; i >= 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                Bundle bundle=new Bundle();
                bundle.putInt("Current Count",i);
                Message message=new Message();
                message.setData(bundle);
                messageHandler.sendMessage(message);
            }

            progressDialog.dismiss();
        }
    }

   private class MessageHandler extends Handler {
        public void handleMessage(Message message) {
            int CurrentCount=message.getData().getInt("Current Count");
            progressDialog.setMessage("Plz Wait..."+CurrentCount);
            if(CurrentCount==0){
                doNotify();
            }
        }

    }
}