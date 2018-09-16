package great.ufc.br.awarenessclass;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;

import great.ufc.br.awarenessclass.actions.NotificationAction;
import great.ufc.br.awarenessclass.actions.VibrateAction;

public class FenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence);

        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
        AwarenessFence walkingFence = DetectedActivityFence.during(DetectedActivityFence.WALKING);
        AwarenessFence compositeFence = AwarenessFence.and(headphoneFence,walkingFence);
        //Filters
        IntentFilter headphoneFilter = new IntentFilter("headphone");
        IntentFilter walkingFilter = new IntentFilter("walking");
        IntentFilter compositeFilter = new IntentFilter("composite");
        //receivers
        registerReceiver(new NotificationAction(),headphoneFilter);
        registerReceiver(new VibrateAction(),walkingFilter);
        registerReceiver(new VibrateAction(),compositeFilter);
        //pending intents
        PendingIntent headphonePending = PendingIntent.getBroadcast(this,777,new Intent("headphone"),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent walkingPending = PendingIntent.getBroadcast(this,777,new Intent("walking"),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent compositePending = PendingIntent.getBroadcast(this,777,new Intent("composite"),PendingIntent.FLAG_CANCEL_CURRENT);
        //Fence Registration
        FenceClient client = Awareness.getFenceClient(this);
        client.updateFences(new FenceUpdateRequest.Builder()
        .addFence("headphoneFence",headphoneFence,headphonePending)
        .addFence("walking",walkingFence,walkingPending)
        .addFence("composite",compositeFence,compositePending)
        .build());
    }
}
