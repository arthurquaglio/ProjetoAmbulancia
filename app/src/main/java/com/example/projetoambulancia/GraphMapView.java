package com.example.projetoambulancia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.projetoambulancia.data.GraphData;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMapView extends View {

    private final Paint nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint unitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint infoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint infoTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private GraphData data;
    private final Map<Integer, float[]> lastPositions = new HashMap<>();
    private final RectF infoBox = new RectF();

    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1f;
    private float translateX = 0f;
    private float translateY = 0f;
    private float lastTouchX;
    private float lastTouchY;
    private boolean dragging;
    private Integer selectedBairroId;

    public interface OnBairroSelectedListener {
        void onBairroSelected(Integer bairroId, float screenX, float screenY);
    }

    private OnBairroSelectedListener onBairroSelectedListener;

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
        int nodeColor = ContextCompat.getColor(getContext(), R.color.brand_primary);
        int unitColor = ContextCompat.getColor(getContext(), R.color.brand_secondary);
        int selectedColor = ContextCompat.getColor(getContext(), R.color.brand_tertiary);
        int edgeColor = resolveThemeColor(android.R.attr.textColorSecondary, 0xFF94A3B8);
        int textColor = resolveThemeColor(android.R.attr.textColorPrimary, 0xFF111827);
        int surfaceColor = resolveThemeColor(android.R.attr.colorBackground, 0xFFFFFFFF);

        nodePaint.setColor(nodeColor);
        unitPaint.setColor(unitColor);
        edgePaint.setColor(edgeColor);
        edgePaint.setStrokeWidth(3f);
        textPaint.setColor(textColor);
        textPaint.setTextSize(28f);

        selectedPaint.setColor(selectedColor);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(6f);

        infoPaint.setColor(surfaceColor);
        infoPaint.setStyle(Paint.Style.FILL);
        infoTextPaint.setColor(textColor);
        infoTextPaint.setTextSize(30f);

        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor = Math.max(0.6f, Math.min(scaleFactor * detector.getScaleFactor(), 2.5f));
                invalidate();
                return true;
            }
        });

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                handleTap(e.getX(), e.getY());
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                translateX -= distanceX;
                translateY -= distanceY;
                invalidate();
                return true;
            }
        });
    }

    private int resolveThemeColor(int attr, int fallback) {
        TypedValue value = new TypedValue();
        if (getContext().getTheme().resolveAttribute(attr, value, true)) {
            if (value.resourceId != 0) {
                return ContextCompat.getColor(getContext(), value.resourceId);
            }
            return value.data;
        }
        return fallback;
    }

    public void setData(GraphData data) {
        this.data = data;
        invalidate();
    }

    public void setOnBairroSelectedListener(OnBairroSelectedListener listener) {
        this.onBairroSelectedListener = listener;
    }

    public void clearSelection() {
        selectedBairroId = null;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dragging = true;
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragging && !scaleDetector.isInProgress()) {
                    float dx = event.getX() - lastTouchX;
                    float dy = event.getY() - lastTouchY;
                    translateX += dx;
                    translateY += dy;
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dragging = false;
                break;
            default:
                break;
        }
        return true;
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

        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.scale(scaleFactor, scaleFactor, centerX, centerY);

        lastPositions.clear();
        int total = bairros.size();
        for (int i = 0; i < total; i++) {
            double angle = (2 * Math.PI * i) / total - Math.PI / 2;
            float x = (float) (centerX + radius * Math.cos(angle));
            float y = (float) (centerY + radius * Math.sin(angle));
            lastPositions.put(bairros.get(i), new float[]{x, y});
        }

        for (Map.Entry<Integer, List<Integer>> entry : data.getAdjacencias().entrySet()) {
            float[] origem = lastPositions.get(entry.getKey());
            if (origem == null) {
                continue;
            }
            for (int destinoId : entry.getValue()) {
                float[] destino = lastPositions.get(destinoId);
                if (destino != null) {
                    canvas.drawLine(origem[0], origem[1], destino[0], destino[1], edgePaint);
                }
            }
        }

        for (int bairroId : bairros) {
            float[] pos = lastPositions.get(bairroId);
            if (pos == null) {
                continue;
            }
            boolean temUnidade = !data.getUnidadesPorBairro(bairroId).isEmpty();
            Paint paint = temUnidade ? unitPaint : nodePaint;
            canvas.drawCircle(pos[0], pos[1], 14f, paint);
            if (selectedBairroId != null && selectedBairroId == bairroId) {
                canvas.drawCircle(pos[0], pos[1], 20f, selectedPaint);
            }
            String nome = data.getBairroNomePorId(bairroId);
            if (nome != null) {
                canvas.drawText(nome, pos[0] + 16f, pos[1] - 10f, textPaint);
            }
        }

        canvas.restore();
    }

    private void handleTap(float x, float y) {
        if (lastPositions.isEmpty()) {
            return;
        }

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float graphX = (x - translateX - centerX) / scaleFactor + centerX;
        float graphY = (y - translateY - centerY) / scaleFactor + centerY;

        float minDistance = Float.MAX_VALUE;
        Integer nearest = null;
        for (Map.Entry<Integer, float[]> entry : lastPositions.entrySet()) {
            float[] pos = entry.getValue();
            float dx = graphX - pos[0];
            float dy = graphY - pos[1];
            float dist = (float) Math.hypot(dx, dy);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = entry.getKey();
            }
        }

        if (minDistance <= 32f) {
            selectedBairroId = nearest;
        } else {
            selectedBairroId = null;
        }
        if (onBairroSelectedListener != null) {
            onBairroSelectedListener.onBairroSelected(selectedBairroId, x, y);
        }
        invalidate();
    }
}
