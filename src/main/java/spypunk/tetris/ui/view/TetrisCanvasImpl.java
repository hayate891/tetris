package spypunk.tetris.ui.view;

import static spypunk.tetris.constants.TetrisConstants.HEIGHT;
import static spypunk.tetris.constants.TetrisConstants.WIDTH;
import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.ui.controller.TetrisController;

@Singleton
public class TetrisCanvasImpl implements TetrisCanvas {

    private static final int BUFFER_STATEGIES = 3;

    private static final Dimension DEFAULT_DIMENSION = new Dimension(
            WIDTH * BLOCK_SIZE + 9 * BLOCK_SIZE,
            (HEIGHT - 2) * BLOCK_SIZE + 2 * BLOCK_SIZE);

    private final class GameCanvasKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            gameController.onKeyPressed(e.getKeyCode());
        }
    }

    private final TetrisController gameController;

    private final BufferStrategy bufferStrategy;

    @Inject
    public TetrisCanvasImpl(TetrisController gameController, TetrisFrame gameFrame) {
        this.gameController = gameController;

        Canvas canvas = new Canvas();

        canvas.setPreferredSize(DEFAULT_DIMENSION);
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(true);
        canvas.addKeyListener(new GameCanvasKeyAdapter());

        gameFrame.getContentPane().add(canvas, BorderLayout.CENTER);
        gameFrame.pack();

        canvas.createBufferStrategy(BUFFER_STATEGIES);

        bufferStrategy = canvas.getBufferStrategy();
    }

    @Override
    public void render(Consumer<Graphics2D> consumer) {
        do {
            do {
                final Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height);

                consumer.accept(graphics);

                graphics.dispose();
            } while (bufferStrategy.contentsRestored());

            bufferStrategy.show();

        } while (bufferStrategy.contentsLost());
    }

}