package octopode.gecw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class Dashboard extends AppCompatActivity
{
    CustomGauge temp,humi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        temp = findViewById(R.id.temperature);
        humi = findViewById(R.id.humidity);

        temp.setPointSize(170);
    }
}
