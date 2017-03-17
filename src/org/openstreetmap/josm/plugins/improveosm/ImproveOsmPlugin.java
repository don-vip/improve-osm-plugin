/*
 *  Copyright 2015 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.improveosm;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.data.Preferences.PreferenceChangedListener;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.NavigatableComponent;
import org.openstreetmap.josm.gui.NavigatableComponent.ZoomChangeListener;
import org.openstreetmap.josm.gui.datatransfer.ClipboardUtils;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerAddEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerChangeListener;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerOrderChangeEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerRemoveEvent;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.InfoDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.AbstractLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.ImproveOsmLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.Keys;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.ImageProvider;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Defines the main functionality of the Improve-OSM plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public class ImproveOsmPlugin extends Plugin implements LayerChangeListener, ZoomChangeListener,
PreferenceChangedListener, MouseListener, CommentObserver, TurnRestrictionSelectionObserver {

    private static final String COPY_ACTION = "Copy";

    /* layers associated with this plugin */
    private MissingGeometryLayer missingGeometryLayer;
    private DirectionOfFlowLayer directionOfFlowLayer;
    private TurnRestrictionLayer turnRestrictionLayer;

    /* toggle dialog associated with this plugin */
    private ImproveOsmDetailsDialog detailsDialog;

    private static final int SEARCH_DELAY = 600;
    private Timer zoomTimer;
    private boolean listenersRegistered = false;

    /* menu items for the layers */
    private JMenuItem missingGeometryLayerMenuItem;
    private JMenuItem directionOfFlowLayerMenuItem;
    private JMenuItem turnRestrictionLayerLayerMenuItem;


    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInfo} object
     */
    public ImproveOsmPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);
    }


    @Override
    public PreferenceSetting getPreferenceSetting() {
        return new PreferenceEditor();
    }

    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (Main.map != null && !GraphicsEnvironment.isHeadless()) {
            // create details dialog
            detailsDialog = new ImproveOsmDetailsDialog();
            detailsDialog.registerCommentObserver(this);
            detailsDialog.registerTurnRestrictionSelectionObserver(this);
            newMapFrame.addToggleDialog(detailsDialog);

            // enable dialog
            if (PreferenceManager.getInstance().loadPanelOpenedFlag()) {
                detailsDialog.showDialog();
            } else {
                detailsDialog.hideDialog();
            }

            initializeLayerMenuItems();

            // add layers
            if (PreferenceManager.getInstance().loadMissingGeometryLayerOpenedFlag()) {
                missingGeometryLayer = new MissingGeometryLayer();
                Main.getLayerManager().addLayer(missingGeometryLayer);
            }
            if (PreferenceManager.getInstance().loadDirectionOfFlowLayerOpenedFlag()) {
                directionOfFlowLayer = new DirectionOfFlowLayer();
                Main.getLayerManager().addLayer(directionOfFlowLayer);
            }
            if (PreferenceManager.getInstance().loadTurnRestrictionLayerOpenedFlag()) {
                turnRestrictionLayer = new TurnRestrictionLayer();
                Main.getLayerManager().addLayer(turnRestrictionLayer);
            }
            if (missingGeometryLayer != null || directionOfFlowLayer != null || turnRestrictionLayer != null) {
                registerListeners();
            }
        }
        if (oldMapFrame != null && newMapFrame == null) {
            missingGeometryLayerMenuItem.setEnabled(false);
            directionOfFlowLayerMenuItem.setEnabled(false);
            turnRestrictionLayerLayerMenuItem.setEnabled(false);
        }
    }


    private void registerListeners() {
        if (!listenersRegistered) {
            NavigatableComponent.addZoomChangeListener(ImproveOsmPlugin.this);
            Main.getLayerManager().addLayerChangeListener(ImproveOsmPlugin.this);
            Main.pref.addPreferenceChangeListener(ImproveOsmPlugin.this);
            Main.map.mapView.addMouseListener(ImproveOsmPlugin.this);
            Main.map.mapView.registerKeyboardAction(new CopyAction(), COPY_ACTION,
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                    JComponent.WHEN_FOCUSED);
            listenersRegistered = true;
        }
    }

    private void initializeLayerMenuItems() {
        if (missingGeometryLayerMenuItem == null) {
            missingGeometryLayerMenuItem = MainMenu.add(Main.main.menu.imageryMenu,
                    new LayerActivator(DataLayer.MISSING_GEOMETRY,
                            MissingGeometryGuiConfig.getInstance().getLayerName(),
                            IconConfig.getInstance().getMissingGeometryLayerIconName()),
                    false);
        }
        missingGeometryLayerMenuItem.setEnabled(true);

        if (directionOfFlowLayerMenuItem == null) {
            directionOfFlowLayerMenuItem = MainMenu.add(Main.main.menu.imageryMenu,
                    new LayerActivator(DataLayer.DIRECTION_OF_FLOW,
                            DirectionOfFlowGuiConfig.getInstance().getLayerName(),
                            IconConfig.getInstance().getDirectionOfFlowLayerIconName()),
                    false);
        }
        directionOfFlowLayerMenuItem.setEnabled(true);

        if (turnRestrictionLayerLayerMenuItem == null) {
            turnRestrictionLayerLayerMenuItem = MainMenu.add(Main.main.menu.imageryMenu,
                    new LayerActivator(DataLayer.TURN_RESTRICTION,
                            TurnRestrictionGuiConfig.getInstance().getLayerName(),
                            IconConfig.getInstance().getTurnRestrictonLayerIconName()),
                    false);
        }
        turnRestrictionLayerLayerMenuItem.setEnabled(true);
    }


    /* implementation of LayerChangeListener */

    @Override
    public void layerAdded(final LayerAddEvent event) {
        if (event.getAddedLayer() instanceof ImproveOsmLayer) {
            if (event.getAddedLayer() instanceof MissingGeometryLayer) {
                PreferenceManager.getInstance().saveMissingGeometryLayerOpenedFlag(true);
            } else if (event.getAddedLayer() instanceof DirectionOfFlowLayer) {
                PreferenceManager.getInstance().saveDirectionOfFlowLayerOpenedFlag(true);
            } else if (event.getAddedLayer() instanceof TurnRestrictionLayer) {
                PreferenceManager.getInstance().saveTurnRestrictionFiltersChangedFlag(true);
            }
            zoomChanged();
        }
    }


    @Override
    public void layerOrderChanged(final LayerOrderChangeEvent event) {
        final Layer oldLayer =
                Main.getLayerManager().getLayers().size() > 1 ? Main.getLayerManager().getLayers().get(1) : null;
                final Layer newLayer = Main.getLayerManager().getActiveLayer();
                if (oldLayer != null && newLayer != null && newLayer instanceof AbstractLayer) {
                    if (oldLayer instanceof MissingGeometryLayer) {
                        updateSelectedData(missingGeometryLayer, null, null);
                    } else if (oldLayer instanceof DirectionOfFlowLayer) {
                        updateSelectedData(directionOfFlowLayer, null, null);
                    } else if (oldLayer instanceof TurnRestrictionLayer) {
                        updateSelectedData(turnRestrictionLayer, null, null);
                    }
                }
    }


    @Override
    public void layerRemoving(final LayerRemoveEvent event) {
        if (event.getRemovedLayer() instanceof ImproveOsmLayer) {
            final ImproveOsmLayer<?> removedLayer = (ImproveOsmLayer<?>) event.getRemovedLayer();
            if (removedLayer.hasSelectedItems()) {
                removedLayer.updateSelectedItem(null);
                detailsDialog.updateUI(null, null);
            }
        }

        if (event.getRemovedLayer() instanceof MissingGeometryLayer) {
            missingGeometryLayer = null;
        } else if (event.getRemovedLayer() instanceof DirectionOfFlowLayer) {
            directionOfFlowLayer = null;
        } else if (event.getRemovedLayer() instanceof TurnRestrictionLayer) {
            turnRestrictionLayer = null;
        }
        if (missingGeometryLayer == null && directionOfFlowLayer == null && turnRestrictionLayer == null) {
            // remove listeners
            PreferenceManager.getInstance().saveErrorSuppressFlag(false);
            Main.getLayerManager().removeLayerChangeListener(this);
            NavigatableComponent.removeZoomChangeListener(this);
            Main.pref.removePreferenceChangeListener(this);
            if (Main.map != null) {
                Main.map.mapView.removeMouseListener(this);
                detailsDialog.updateUI(null, null);
                listenersRegistered = false;
                detailsDialog.hideDialog();
            }
        }
    }


    /* ZoomChangeListener method */

    @Override
    public void zoomChanged() {
        if (zoomTimer != null && zoomTimer.isRunning()) {
            // if timer is running restart it
            zoomTimer.restart();
        } else {
            zoomTimer = new Timer(SEARCH_DELAY, new ZoomActionListener());
            zoomTimer.setRepeats(false);
            zoomTimer.start();
        }
    }

    private final class ZoomActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (missingGeometryLayer != null && missingGeometryLayer.isVisible()) {
                Main.worker.execute(new MissingGeometryUpdateThread(detailsDialog, missingGeometryLayer));
            }
            if (directionOfFlowLayer != null && directionOfFlowLayer.isVisible()) {
                if (Main.getLayerManager().getActiveLayer() == directionOfFlowLayer) {
                    new InfoDialog().displayDirectionOfFlowEditTip(Util.zoom(Main.map.mapView.getRealBounds()));
                }
                Main.worker.execute(new DirectionOfFlowUpdateThread(detailsDialog, directionOfFlowLayer));
            }
            if (turnRestrictionLayer != null && turnRestrictionLayer.isVisible()) {
                Main.worker.execute(new TurnRestrictionUpdateThread(detailsDialog, turnRestrictionLayer));
            }

            new InfoDialog().displayLocationButtonTip();
            new InfoDialog().displayOldPluginsDialog();
        }
    }


    /* PreferenceChangeListener method */

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            switch (event.getKey()) {
                case Keys.MG_FILTERS_CHANGED:
                    Main.worker.execute(new MissingGeometryUpdateThread(detailsDialog, missingGeometryLayer));
                    break;
                case Keys.DOF_FILTERS_CHANGED:
                    Main.worker.execute(new DirectionOfFlowUpdateThread(detailsDialog, directionOfFlowLayer));
                    break;
                case Keys.TR_FILTERS_CHANGED:
                    Main.worker.execute(new TurnRestrictionUpdateThread(detailsDialog, turnRestrictionLayer));
                    break;
                case Keys.PANEL_ICON_VISIBILITY:
                    PreferenceManager.getInstance().savePanelOpenedFlag(event.getNewValue().toString());
                    break;
                default:
                    // ignore it
                    break;
            }
        }
    }


    /* MouseListener implementation */

    @Override
    public void mouseClicked(final MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            final Layer activeLayer = Main.getLayerManager().getActiveLayer();
            final Point point = event.getPoint();
            final boolean multiSelect = event.isShiftDown();
            if (Util.zoom(Main.map.mapView.getRealBounds()) > Config.getInstance().getMaxClusterZoom()) {
                if (activeLayer instanceof MissingGeometryLayer) {
                    // select tiles
                    selectItem(ServiceHandler.getMissingGeometryHandler(), missingGeometryLayer, point, multiSelect);
                } else if (activeLayer instanceof DirectionOfFlowLayer) {
                    // select road segments
                    selectItem(ServiceHandler.getDirectionOfFlowHandler(), directionOfFlowLayer, point, multiSelect);
                } else if (activeLayer instanceof TurnRestrictionLayer) {
                    selectTurnRestriction(point, multiSelect);
                }
            }
        }
    }

    private <T> void selectItem(final ServiceHandler<T> handler, final ImproveOsmLayer<T> layer, final Point point,
            final boolean multiSelect) {
        final T item = layer.nearbyItem(point, multiSelect);
        if (item != null) {
            if (!item.equals(layer.lastSelectedItem())) {
                retrieveComments(handler, layer, item);
            }
        } else {
            // clear selection
            updateSelectedData(layer, null, null);
        }
    }

    private void selectTurnRestriction(final Point point, final boolean multiSelect) {
        final TurnRestriction turnRestriction = turnRestrictionLayer.nearbyItem(point, multiSelect);
        boolean shouldSelect = false;
        if (turnRestriction != null) {
            if (multiSelect) {
                if (turnRestriction.getTurnRestrictions() != null) {
                    shouldSelect = turnRestrictionLayer.getSelectedItems().isEmpty();
                } else {
                    final TurnRestriction lastSelectedItem = turnRestrictionLayer.lastSelectedItem();
                    if ((lastSelectedItem == null) || (lastSelectedItem.getTurnRestrictions() == null)) {
                        shouldSelect = true;
                    }
                }
            } else {
                shouldSelect = true;
            }
        }
        if (shouldSelect) {
            List<Comment> comments = null;
            if (turnRestriction.getTurnRestrictions() == null
                    && !turnRestriction.equals(turnRestrictionLayer.lastSelectedItem())) {
                comments = ServiceHandler.getTurnRestrictionHandler().retrieveComments(turnRestriction);
            }
            updateSelectedData(turnRestrictionLayer, turnRestriction, comments);
        } else if (!multiSelect) {
            // clear selection
            updateSelectedData(turnRestrictionLayer, null, null);
        }
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        // no logic for this action
    }


    /* TurnRestrictionSelectionObserver implementation */

    @Override
    public void selectSimpleTurnRestriction(final TurnRestriction turnRestriction) {
        final List<Comment> comments = ServiceHandler.getTurnRestrictionHandler().retrieveComments(turnRestriction);
        turnRestrictionLayer.updateSelectedItem(null);
        updateSelectedData(turnRestrictionLayer, turnRestriction, comments);
    }


    /* CommentObserver method */

    @Override
    public synchronized void createComment(final Comment comment) {
        final List<Layer> layers = Main.getLayerManager().getLayers();
        Main.worker.execute(new Runnable() {

            @Override
            public void run() {
                if (layers.contains(missingGeometryLayer) && missingGeometryLayer.hasSelectedItems()) {
                    createComment(ServiceHandler.getMissingGeometryHandler(), missingGeometryLayer,
                            new MissingGeometryUpdateThread(detailsDialog, missingGeometryLayer), comment);
                } else if (layers.contains(directionOfFlowLayer) && directionOfFlowLayer.hasSelectedItems()) {
                    createComment(ServiceHandler.getDirectionOfFlowHandler(), directionOfFlowLayer,
                            new DirectionOfFlowUpdateThread(detailsDialog, directionOfFlowLayer), comment);
                } else if (layers.contains(turnRestrictionLayer) && turnRestrictionLayer.hasSelectedItems()) {
                    createComment(ServiceHandler.getTurnRestrictionHandler(), turnRestrictionLayer,
                            new TurnRestrictionUpdateThread(detailsDialog, turnRestrictionLayer), comment);
                }
            }
        });
    }

    private <T> void createComment(final ServiceHandler<T> handler, final ImproveOsmLayer<T> layer,
            final UpdateThread<T> updateThread, final Comment comment) {
        PreferenceManager.getInstance().saveLastComment(layer, comment.getText());
        final List<T> items = layer.getSelectedItems();
        handler.comment(comment, items);

        if (comment.getStatus() != null) {
            // status changed - refresh data (possible to select only 1 status from filters)

            if (!Main.getLayerManager().getActiveLayer().equals(layer)) {
                updateSelectedData(layer, null, null);
            }
            Main.worker.execute(updateThread);
        } else {
            if (items.equals(layer.getSelectedItems())) {
                final T item = items.get(items.size() - 1);
                retrieveComments(handler, layer, item);
            }
        }
    }


    /* commonly used private methods and classes */


    private <T> void retrieveComments(final ServiceHandler<T> handler, final ImproveOsmLayer<T> layer, final T item) {
        final List<Comment> comments = handler.retrieveComments(item);
        updateSelectedData(layer, item, comments);
    }

    private <T> void updateSelectedData(final ImproveOsmLayer<T> layer, final T item, final List<Comment> comments) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                detailsDialog.updateUI(item, comments);
                layer.updateSelectedItem(item);
                Main.map.repaint();
            }
        });
    }

    private final class MissingGeometryUpdateThread extends UpdateThread<Tile> {

        private MissingGeometryUpdateThread(final ImproveOsmDetailsDialog detailsDialog,
                final ImproveOsmLayer<Tile> layer) {
            super(detailsDialog, layer);
        }

        @Override
        DataSet<Tile> searchData(final BoundingBox bbox, final int zoom) {
            final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
            return ServiceHandler.getMissingGeometryHandler().search(bbox, filter, zoom);
        }

        @Override
        boolean shouldClearSelection(final Tile item) {
            final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
            boolean result = filter.getStatus() == item.getStatus();
            result = result && (filter.getTypes() == null || filter.getTypes().contains(item.getType()));
            result = result && (filter.getCount() == null || filter.getCount() <= item.getNumberOfTrips());
            return !result;
        }
    }

    private final class DirectionOfFlowUpdateThread extends UpdateThread<RoadSegment> {

        private DirectionOfFlowUpdateThread(final ImproveOsmDetailsDialog detailsDialog,
                final ImproveOsmLayer<RoadSegment> layer) {
            super(detailsDialog, layer);
        }

        @Override
        DataSet<RoadSegment> searchData(final BoundingBox bbox, final int zoom) {
            final OnewayFilter filter = PreferenceManager.getInstance().loadOnewayFilter();
            return ServiceHandler.getDirectionOfFlowHandler().search(bbox, filter, zoom);
        }

        @Override
        boolean shouldClearSelection(final RoadSegment item) {
            final OnewayFilter filter = PreferenceManager.getInstance().loadOnewayFilter();
            boolean result = filter.getConfidenceLevels() == null
                    || filter.getConfidenceLevels().contains(item.getConfidenceLevel());
            result = result && filter.getStatus().equals(item.getStatus());
            return !result;
        }
    }

    private final class TurnRestrictionUpdateThread extends UpdateThread<TurnRestriction> {

        private TurnRestrictionUpdateThread(final ImproveOsmDetailsDialog detailsDialog,
                final ImproveOsmLayer<TurnRestriction> layer) {
            super(detailsDialog, layer);
        }

        @Override
        DataSet<TurnRestriction> searchData(final BoundingBox bbox, final int zoom) {
            final TurnRestrictionFilter filter = PreferenceManager.getInstance().loadTurnRestrictionFilter();
            return ServiceHandler.getTurnRestrictionHandler().search(bbox, filter, zoom);
        }

        @Override
        boolean shouldClearSelection(final TurnRestriction item) {
            final TurnRestrictionFilter filter = PreferenceManager.getInstance().loadTurnRestrictionFilter();
            boolean result = (filter.getConfidenceLevels() == null
                    || filter.getConfidenceLevels().contains(item.getConfidenceLevel()));
            result = result && filter.getStatus().equals(item.getStatus());
            return !result;
        }
    }


    private final class CopyAction extends AbstractAction {

        private static final long serialVersionUID = -6108419035272335873L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getActionCommand().equals(COPY_ACTION)) {
                final Layer activeLayer = Main.getLayerManager().getActiveLayer();
                String selection = "";
                if (activeLayer instanceof MissingGeometryLayer) {
                    if (missingGeometryLayer.hasSelectedItems()) {
                        selection = missingGeometryLayer.getSelectedItems().toString();
                    }
                } else if (activeLayer instanceof DirectionOfFlowLayer) {
                    if (directionOfFlowLayer.hasSelectedItems()) {
                        selection = directionOfFlowLayer.getSelectedItems().toString();
                    }
                } else if (activeLayer instanceof TurnRestrictionLayer) {
                    if (turnRestrictionLayer.hasSelectedItems()) {
                        selection = turnRestrictionLayer.getSelectedItems().toString();
                    }
                }
                ClipboardUtils.copyString(selection);
            }
        }
    }


    private class LayerActivator extends JosmAction {

        private static final long serialVersionUID = 383609516179512054L;
        private final DataLayer dataLayer;

        private LayerActivator(final DataLayer dataLayer, final String layerName, final String iconName) {
            super(layerName, new ImageProvider(iconName), null, null, false, null, false);
            this.dataLayer = dataLayer;
            setEnabled(false);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (!listenersRegistered) {
                NavigatableComponent.addZoomChangeListener(ImproveOsmPlugin.this);
                Main.getLayerManager().addLayerChangeListener(ImproveOsmPlugin.this);
                Main.pref.addPreferenceChangeListener(ImproveOsmPlugin.this);
                Main.map.mapView.addMouseListener(ImproveOsmPlugin.this);
                Main.map.mapView.registerKeyboardAction(new CopyAction(), COPY_ACTION,
                        KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                        JComponent.WHEN_FOCUSED);
                listenersRegistered = true;
            }
            switch (dataLayer) {
                case MISSING_GEOMETRY:
                    if (missingGeometryLayer == null) {
                        // add layer
                        PreferenceManager.getInstance().saveMissingGeometryLayerOpenedFlag(true);
                        missingGeometryLayer = new MissingGeometryLayer();
                        Main.getLayerManager().addLayer(missingGeometryLayer);
                    }
                    break;
                case DIRECTION_OF_FLOW:
                    if (directionOfFlowLayer == null) {
                        directionOfFlowLayer = new DirectionOfFlowLayer();
                        Main.getLayerManager().addLayer(directionOfFlowLayer);
                        PreferenceManager.getInstance().saveDirectionOfFlowLayerOpenedFlag(true);
                    }
                    break;
                default:
                    // turn restriction
                    if (turnRestrictionLayer == null) {
                        turnRestrictionLayer = new TurnRestrictionLayer();
                        Main.getLayerManager().addLayer(turnRestrictionLayer);
                        PreferenceManager.getInstance().saveTurnRestrictionFiltersChangedFlag(true);
                    }
                    break;
            }
        }

    }
}