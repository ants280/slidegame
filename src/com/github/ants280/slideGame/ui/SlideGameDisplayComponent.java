package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import com.github.ants280.slideGame.logic.Tile;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * The component the slideGame is painted on.
 */
public class SlideGameDisplayComponent extends JComponent
{
	private final Grid grid;
	private static final RenderingHints ANTIALIAS_ON_RENDERING_HINT
			= new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
	private Font tileFont;

	public SlideGameDisplayComponent(Grid grid)
	{
		super();

		this.grid = grid;
		this.tileFont = new Font("times", Font.PLAIN, 12);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		((Graphics2D) g).setRenderingHints(ANTIALIAS_ON_RENDERING_HINT);
		// TODO: the font size only needs to be changed when the grid size (length/dimension) changes, not on every paint call.
		double fontSize = (getHeight() / (grid.getLength() * 3d));
		tileFont = tileFont.deriveFont((float) fontSize);
		g.setFont(tileFont);

		int width = this.getWidth();
		int height = this.getHeight();
		int minDimension = Math.min(width, height);
		double xOffset = (width - minDimension) / 2d;
		double yOffset = (height - minDimension) / 2d;
		int gridLength = grid.getLength();
		double cellSize = minDimension / gridLength;
		// Painting may leave gaps between tiles and spacers if not rounded.
		double tileSize = round(cellSize * 0.90d);
		double spacerSize = cellSize - tileSize;
		double halfSpacerSize = spacerSize / 2d;

		paintGrid(
				gridLength,
				g,
				xOffset,
				yOffset,
				minDimension,
				cellSize,
				spacerSize,
				halfSpacerSize);
		paintTiles(
				gridLength,
				g,
				xOffset,
				yOffset,
				cellSize,
				tileSize,
				halfSpacerSize);
	}

	private void paintGrid(
			int gridLength,
			Graphics g,
			double xOffset,
			double yOffset,
			double minDimension,
			double cellSize,
			double spacerSize,
			double halfSpacerSize)
	{
		g.setColor(SlideGameColors.SPACER_COLOR);
		for (int c = 0; c <= gridLength; c++)
		{
			int x = round(xOffset + (c * cellSize) - halfSpacerSize);
			int y = round(yOffset);
			int verticalLineWidth = round(spacerSize);
			int height = round(minDimension);
			g.fillRect(
					x, y,
					verticalLineWidth, height);
		}
		for (int r = 0; r <= gridLength; r++)
		{
			int x = round(xOffset);
			int y = round(yOffset + (r * cellSize) - halfSpacerSize);
			int width = round(minDimension);
			int horizontalLineHeight = round(spacerSize);
			g.fillRect(
					x, y,
					width, horizontalLineHeight);
		}
	}

	private void paintTiles(
			int gridLength,
			Graphics g,
			double xOffset,
			double yOffset,
			double cellSize,
			double tileSize,
			double halfSpacerSize)
	{
		for (int c = 0; c < gridLength; c++)
		{
			for (int r = 0; r < gridLength; r++)
			{
				paintTile(
						g,
						c,
						r,
						xOffset,
						yOffset,
						cellSize,
						tileSize,
						halfSpacerSize);
			}
		}
	}

	private void paintTile(
			Graphics g,
			int c,
			int r,
			double xOffset,
			double yOffset,
			double cellSize,
			double tileSize,
			double halfSpacerSize)
	{
		Tile tile = grid.getTile(c, r);
		if (tile != null)
		{
			paintTileBackground(
					SlideGameColors.getColor(tile),
					g,
					c,
					r,
					xOffset,
					yOffset,
					cellSize,
					tileSize,
					halfSpacerSize);
			paintTileText(
					tile.getDisplayValue(),
					g,
					c,
					r,
					xOffset,
					yOffset,
					cellSize);
		}
	}

	private void paintTileBackground(
			Color tileColor,
			Graphics g,
			int c,
			int r,
			double xOffset,
			double yOffset,
			double cellSize,
			double tileSize,
			double halfSpacerSize)
	{
		g.setColor(tileColor);
		int x = round(xOffset + (c * cellSize) + halfSpacerSize);
		int y = round(yOffset + (r * cellSize) + halfSpacerSize);
		int width = round(tileSize);
		int height = round(tileSize);
		g.fillRect(x, y, width, height);
	}

	private void paintTileText(
			String tileText,
			Graphics g,
			int c,
			int r,
			double xOffset,
			double yOffset,
			double cellSize)
	{
		g.setColor(SlideGameColors.TILE_TEXT_COLOR);
		double fontHeightPx = tileFont.getSize() * 0.75d;
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(tileText);
		int x = round(xOffset + (((c + 0.5d) * cellSize) - (textWidth / 2)));
		int y = round(yOffset + (((r + 0.5d) * cellSize) + (fontHeightPx / 2)));
		g.drawString(tileText, x, y);
	}

	private static int round(double value)
	{
		return Math.toIntExact(Math.round(value));
	}
}
