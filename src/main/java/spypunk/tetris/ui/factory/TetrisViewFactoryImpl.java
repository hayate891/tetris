package spypunk.tetris.ui.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.view.TetrisInstanceInfoView;
import spypunk.tetris.ui.view.TetrisInstanceInfoViewImpl;
import spypunk.tetris.ui.view.TetrisInstanceStatisticsView;
import spypunk.tetris.ui.view.TetrisInstanceStatisticsViewImpl;
import spypunk.tetris.ui.view.TetrisInstanceView;
import spypunk.tetris.ui.view.TetrisInstanceViewImpl;
import spypunk.tetris.ui.view.TetrisView;
import spypunk.tetris.ui.view.TetrisViewImpl;

@Singleton
public class TetrisViewFactoryImpl implements TetrisViewFactory {

    private final TetrisController tetrisController;

    private final FontFactory fontFactory;

    private final ImageFactory imageFactory;

    @Inject
    public TetrisViewFactoryImpl(TetrisController tetrisController, FontFactory fontFactory,
            ImageFactory imageFactory) {
        this.tetrisController = tetrisController;
        this.fontFactory = fontFactory;
        this.imageFactory = imageFactory;
    }

    @Override
    public TetrisView createTetrisView(Tetris tetris) {
        final TetrisInstanceView tetrisInstanceView = createTetrisInstanceView(tetris);

        return new TetrisViewImpl(tetrisController, tetrisInstanceView, fontFactory, tetris);
    }

    private TetrisInstanceView createTetrisInstanceView(Tetris tetris) {
        final TetrisInstanceStatisticsView tetrisInstanceStatisticsView = createTetrisInstanceStatisticsView(tetris);
        final TetrisInstanceInfoView tetrisInstanceInfoView = createTetrisInstanceInfoView(tetris);

        return new TetrisInstanceViewImpl(fontFactory, tetrisInstanceStatisticsView,
                tetrisInstanceInfoView, imageFactory, tetris);
    }

    private TetrisInstanceStatisticsView createTetrisInstanceStatisticsView(Tetris tetris) {
        return new TetrisInstanceStatisticsViewImpl(fontFactory, imageFactory, tetris);
    }

    private TetrisInstanceInfoView createTetrisInstanceInfoView(Tetris tetris) {
        return new TetrisInstanceInfoViewImpl(fontFactory, imageFactory, tetris);
    }
}
