/*
 ~ CHECK RAMPA - v2.3

 ~      Copyright (C) 2014 Core3 Software, Inc.

 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at

 ~   http://www.apache.org/licenses/LICENSE-2.0

 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.


 ~  @Author Lucas "C0rey" Cavalcante
 ~  @Version 2.3.0
 ~
 */

package com.core3soft.checkrampa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;


public class Result extends Activity {
    // Variables
    String Height, Length;
    Double Inclination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Bundle params to getExtras from intent
        Bundle params = getIntent().getExtras();

        // Variables for receive the params
        Height = params.getString("Altura");
        Length = params.getString("Comprimento");
        // call function 'calculate' parsing parameters Height and Length
        // Height: Received from activity main
        // Length: Received from activity main
        calculate(Double.parseDouble(Height), Double.parseDouble(Length));
    }

    @Override
    public void onBackPressed(){ finish(); }

    View.OnClickListener buttonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // This method is used to search the LENGTH recomend to the height of the ramp
            // Only called if the ramp is INCORRECT
            // Called on click in button 'corrigir'
            Double length;
            if(Double.parseDouble(Height) >= 100){
                length = (Double.parseDouble(Height) * 100) / 5; // Inclination: 5%
            } else if(Double.parseDouble(Height) > 80 && Double.parseDouble(Height) < 100){
                length = (Double.parseDouble(Height) * 100) / 6.25; // Inclination: 6.25%
            } else length = (Double.parseDouble(Height) * 100) / 8.33; // Inclination: 8,33%
            calculate(Double.parseDouble(Height), length);
        }
    };

    public void calculate(Double height, Double length){
        // calculate inclination
        Double rInclination = (height / Math.sqrt((height * height) + (length * length))) * 100;
        // set scale to 2
        BigDecimal bigInclination = new BigDecimal(rInclination);
        bigInclination = bigInclination.setScale(2, BigDecimal.ROUND_HALF_UP);
        bigInclination = BigDecimal.valueOf(bigInclination.doubleValue());
        // parse double value to Inclination var
        Inclination = bigInclination.doubleValue();

        // set values to textviews
        ((TextView) findViewById(R.id.result_height)).setText(height + " cm");
        ((TextView) findViewById(R.id.result_length)).setText(length + " cm");
        ((TextView) findViewById(R.id.result_inclination)).setText(Inclination + "%");

        // check if the ramp is correct or incorrect
        if (height >= 100) { // Inclination: 5% MAX
            if (Inclination > 5) { // incorrect
                showResult("RAMPA INCORRETA");
            } else {
                showResult("RAMPA CORRETA");
            }
        } else if (height > 80 && height < 100) { // Inclination: 6,25% MAX
            if (Inclination > 6.25) { // incorrect
                showResult("RAMPA INCORRETA");
            } else {
                showResult("RAMPA CORRETA");
            }
        } else { // Inclination: 8,33% MAX
            if (Inclination > 8.33) { // incorrect
                showResult("RAMPA INCORRETA");
            } else {
                showResult("RAMPA CORRETA");
            }
        }
    }
    public void showResult(String result){
        if(result.equals("RAMPA CORRETA")){
            ((TextView) findViewById(R.id.result_inclination)).setTextColor(getResources().getColor(R.color.color_correctramp));
            ((TextView) findViewById(R.id.text_finalresult)).setText("RAMPA CORRETA");
            ((TextView) findViewById(R.id.text_finalresult)).setTextColor(getResources().getColor(R.color.color_correctramp));
            ((TextView) findViewById(R.id.result_length)).setTextColor(getResources().getColor(R.color.color_correctramp));
            ((ImageView) findViewById(R.id.imageResult)).setImageDrawable(getResources().getDrawable(R.drawable.ic_ramp_correct));
            findViewById(R.id.corrigir).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) findViewById(R.id.result_inclination)).setTextColor(getResources().getColor(R.color.color_incorrectramp));
            ((TextView) findViewById(R.id.text_finalresult)).setText("RAMPA INCORRETA");
            ((TextView) findViewById(R.id.text_finalresult)).setTextColor(getResources().getColor(R.color.color_incorrectramp));
            ((TextView) findViewById(R.id.result_length)).setTextColor(getResources().getColor(R.color.color_incorrectramp));
            ((ImageView) findViewById(R.id.imageResult)).setImageDrawable(getResources().getDrawable(R.drawable.ic_ramp_incorrect));
            findViewById(R.id.corrigir).setVisibility(View.VISIBLE);
            findViewById(R.id.corrigir).setOnClickListener(buttonHandler);
        }
    }
}