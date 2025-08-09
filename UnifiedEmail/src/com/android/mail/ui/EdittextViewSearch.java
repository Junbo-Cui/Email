/*
 * Copyright (C) 2012 Google Inc.
 * Licensed to The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.mail.ui;

import android.widget.EditText;
import com.android.mail.R;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;


public class EdittextViewSearch extends EditText {
    private Paint paint;
 
    public EdittextViewSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置画笔的属性
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
        //设置画笔颜色为白色
        paint.setColor(Color.WHITE);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**canvas画直线，从左下角到右下角，this.getHeight()-2是获得父edittext的高度，但是必须要-2这样才能保证
         * 画的横线在edittext上面，和原来的下划线的重合
         */
		 Log.i("lhz0319:",""+this.getHeight()+"----"+ this.getWidth());
        canvas.drawLine(9, 41, 231, 41, paint);
    }
}
