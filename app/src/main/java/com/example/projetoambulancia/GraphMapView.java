package com.example.projetoambulancia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.projetoambulancia.data.GraphData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMapView extends View {

    private final Paint nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private GraphData data;

    public GraphMapView(Context context) {
        super(context);
        init();
    }

    public GraphMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        nodePaint.setColor(Color.parseColor("#1565C0"));
        edgePaint.setColor(Color.parseColor("#90A4AE"));
        edgePaint.setStrokeWidth(4f);
        textPaint.setColor(Color.parseColor("#263238"));
        textPaint.setTextSize(28f);
    }

    public void setData(GraphData data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null) {
            return;
        }

        List<Integer> bairros = new ArrayList<>(data.getBairrosOrdenados());
        if (bairros.isEmpty()) {
            return;
        }

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) * 0.35f;

        Map<Integer, float[]> positions = new HashMap<>();
        int total = bairros.size();
        for (int i = 0; i < total; i++) {
            double angle = (2 * Math.PI * i) / total - Math.PI / 2;
            float x = (float) (centerX + radius * Math.cos(angle));
            float y = (float) (centerY + radius * Math.sin(angle));
            positions.put(bairros.get(i), new float[]{x, y});
        }

        for (Map.Entry<Integer, List<Integer>> entry : data.getAdjacencias().entrySet()) {
            float[] origem = positions.get(entry.getKey());
            if (origem == null) {
                continue;
            }
            for (int destinoId : entry.getValue()) {
                float[] destino = positions.get(destinoId);
                if (destino != null) {
                    canvas.drawLine(origem[0], origem[1], destino[0], destino[1], edgePaint);
                }
            }
        }

        for (int bairroId : bairros) {
            float[] pos = positions.get(bairroId);
            if (pos == null) {
                continue;
            }
            canvas.drawCircle(pos[0], pos[1], 14f, nodePaint);
            String nome = data.getBairroNomePorId(bairroId);
            if (nome != null) {
                canvas.drawText(nome, pos[0] + 16f, pos[1] - 10f, textPaint);
            }
        }
    }
}

