/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.BOTH_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.MISSINGGEO_CLUSTER_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.NORMAL_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.PARKING_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.PATH_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.POINT_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.POINT_POS_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.SEL_POINT_POS_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_INVALID_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_LINE_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_OPEN_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_SEL_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_SOLVED_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.WATER_COLOR;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.gui.PaintManager;


/**
 *
 * @author beataj
 * @version $Revision$
 */
final class MissingGeometryHanlder extends PaintHandler<Tile> {

    @Override
    void drawItem(final Graphics2D graphics, final MapView mapView, final Tile tile, final boolean selected) {
        final Color borderColor = tile.getStatus() == Status.OPEN ? TILE_OPEN_COLOR
                : (tile.getStatus() == Status.SOLVED ? TILE_SOLVED_COLOR : TILE_INVALID_COLOR);
        final Color tileColor = tileColor(tile);
        final Composite composite = selected ? TILE_SEL_COMPOSITE : TILE_COMPOSITE;
        final BoundingBox bbox = Util.tileToBoundingBox(tile.getX(), tile.getY());
        final Point northEast = mapView.getPoint(new LatLon(bbox.getNorth(), bbox.getEast()));
        final Point northWest = mapView.getPoint(new LatLon(bbox.getSouth(), bbox.getWest()));
        PaintManager.drawRectangle(graphics, northEast, northWest, NORMAL_COMPOSITE, composite, TILE_LINE_STROKE,
                borderColor, tileColor);

        final int radius = selected ? SEL_POINT_POS_RADIUS : POINT_POS_RADIUS;
        // draw points belonging to the tile
        if (tile.getPoints() != null && tile.getPoints().size() > 1) {
            for (final LatLon latLon : tile.getPoints()) {
                PaintManager.drawCircle(graphics, mapView.getPoint(latLon), POINT_COLOR, radius);
            }
        }
    }

    private Color tileColor(final Tile tile) {
        Color color;
        switch (tile.getType()) {
            case ROAD:
                color = ROAD_COLOR;
                break;
            case PARKING:
                color = PARKING_COLOR;
                break;
            case WATER:
                color = WATER_COLOR;
                break;
            case PATH:
                color = PATH_COLOR;
                break;
            default:
                color = BOTH_COLOR;
                break;
        }
        return color;
    }


    @Override
    Color getClusterColor() {
        return MISSINGGEO_CLUSTER_COLOR;
    }
}