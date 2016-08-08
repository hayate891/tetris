package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_BORDER_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_SIZE;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.common.collect.Lists;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisInstance;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.factory.ImageFactory;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisInstanceInfoViewImpl implements TetrisInstanceInfoView {

    private static final int HEIGHT = 1 + BLOCK_SIZE * 16;

    private static final int WIDTH = 1 + BLOCK_SIZE * 6;

    private static final String SCORE = "SCORE";

    private static final String LEVEL = "LEVEL";

    private static final String NEXT_SHAPE = "NEXT";

    private static final String ROWS = "ROWS";

    private final ImageFactory imageFactory;

    private final JPanel panel;

    private final BufferedImage image;

    private final Rectangle levelRectangle;

    private final Rectangle scoreRectangle;

    private final Rectangle rowsRectangle;

    private final Rectangle nextShapeRectangle;

    private final Rectangle levelLabelRectangle;

    private final Rectangle scoreLabelRectangle;

    private final Rectangle rowsLabelRectangle;

    private final Rectangle nextShapeLabelRectangle;

    private final Map<ShapeType, Rectangle> shapeTypeImageRectangles;

    private final Font defaultFont;

    private final Tetris tetris;

    @Inject
    public TetrisInstanceInfoViewImpl(FontFactory fontFactory, TetrisController tetrisController,
            ImageFactory imageFactory) {
        this.imageFactory = imageFactory;

        tetris = tetrisController.getTetris();
        defaultFont = fontFactory.createDefaultFont(DEFAULT_FONT_SIZE);

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        panel.setOpaque(true);

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        final JLabel label = new JLabel(new ImageIcon(image));

        panel.add(label);

        levelRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE);
        scoreRectangle = new Rectangle(0, BLOCK_SIZE * 4, BLOCK_SIZE * 6, BLOCK_SIZE);
        rowsRectangle = new Rectangle(0, BLOCK_SIZE * 7, BLOCK_SIZE * 6, BLOCK_SIZE);
        nextShapeRectangle = new Rectangle(0, BLOCK_SIZE * 10, BLOCK_SIZE * 6, BLOCK_SIZE * 6);

        levelLabelRectangle = createLabelRectangle(levelRectangle);
        scoreLabelRectangle = createLabelRectangle(scoreRectangle);
        rowsLabelRectangle = createLabelRectangle(rowsRectangle);
        nextShapeLabelRectangle = createLabelRectangle(nextShapeRectangle);

        shapeTypeImageRectangles = Lists.newArrayList(ShapeType.values()).stream()
                .collect(
                    Collectors.toMap(Function.identity(), this::createShapeTypeImageRectangle));
    }

    @Override
    public void update() {
        SwingUtils.doInGraphics(image, this::doUpdate);
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    private void doUpdate(Graphics2D graphics) {
        final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

        renderLevel(graphics, tetrisInstance);
        renderScore(graphics, tetrisInstance);
        renderRows(graphics, tetrisInstance);
        renderNextShape(graphics, tetrisInstance);
    }

    private Rectangle createLabelRectangle(Rectangle rectangle) {
        return new Rectangle(0, rectangle.y - BLOCK_SIZE, rectangle.width,
                BLOCK_SIZE);
    }

    private Rectangle createShapeTypeImageRectangle(ShapeType shapeType) {
        final Image shapeImage = imageFactory
                .createShapeImage(shapeType);
        return SwingUtils.getCenteredImageRectangle(shapeImage, nextShapeRectangle);
    }

    private void renderRows(final Graphics2D graphics, final TetrisInstance tetrisInstance) {
        renderInfo(graphics, rowsRectangle, rowsLabelRectangle, ROWS,
            String.valueOf(tetrisInstance.getCompletedRows()));
    }

    private void renderScore(final Graphics2D graphics, final TetrisInstance tetrisInstance) {
        renderInfo(graphics, scoreRectangle, scoreLabelRectangle, SCORE, String.valueOf(tetrisInstance.getScore()));
    }

    private void renderLevel(final Graphics2D graphics, final TetrisInstance tetrisInstance) {
        renderInfo(graphics, levelRectangle, levelLabelRectangle, LEVEL, String.valueOf(tetrisInstance.getLevel()));
    }

    private void renderNextShape(final Graphics2D graphics, TetrisInstance tetrisInstance) {
        renderLabelAndRectangle(graphics, nextShapeRectangle, nextShapeLabelRectangle, NEXT_SHAPE);

        final ShapeType shapeType = tetrisInstance.getNextShape().getShapeType();

        final Image shapeImage = imageFactory.createShapeImage(shapeType);
        final Rectangle rectangle = shapeTypeImageRectangles.get(shapeType);

        SwingUtils.drawImage(graphics, shapeImage, rectangle);
    }

    private void renderInfo(Graphics2D graphics, Rectangle rectangle, Rectangle labelRectangle,
            String title, String value) {
        renderLabelAndRectangle(graphics, rectangle, labelRectangle, title);

        SwingUtils.renderCenteredText(graphics, value,
            rectangle, defaultFont, DEFAULT_FONT_COLOR);
    }

    private void renderLabelAndRectangle(Graphics2D graphics, Rectangle rectangle, Rectangle labelRectangle,
            String label) {
        SwingUtils.renderCenteredText(graphics, label,
            labelRectangle, defaultFont, DEFAULT_FONT_COLOR);

        graphics.setColor(DEFAULT_BORDER_COLOR);

        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width,
            rectangle.height);
    }
}
