package net.runelite.client.plugins.npchighlight;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Point implements Comparable<Point>
{
	int x, y;

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int compareTo(Point p)
	{
		if (this.x == p.x)
		{
			return this.y - p.y;
		}
		else
		{
			return this.x - p.x;
		}
	}

	public String toString()
	{
		return "(" + x + "," + y + ")";
	}

}

// Algorithm credit goes to
// https://en.wikibooks.org/wiki/Algorithm_Implementation/Geometry/Convex_hull/Monotone_chain#Java
public class ConvexHull
{

	public static long cross(Point O, Point A, Point B)
	{
		return (A.x - O.x) * (B.y - O.y) - (A.y - O.y) * (B.x - O.x);
	}

	public static Point[] convertArea(Area area)
	{
		PathIterator iterator = area.getPathIterator(null);
		float[] floats = new float[6];
		List<Point> points = new ArrayList<>();
		while (!iterator.isDone())
		{
			int type = iterator.currentSegment(floats);
			int x = (int) floats[0];
			int y = (int) floats[1];
			if (type != PathIterator.SEG_CLOSE)
			{
				points.add(new Point(x, y));
			}
			iterator.next();
		}

		return points.toArray(new Point[0]);
	}

	public static Polygon convertPoints(Point[] points)
	{
		Polygon poly = new Polygon();
		for (int i = 0; i < points.length; i++)
		{
			poly.addPoint(points[i].x, points[i].y);
		}

		return poly;
	}

	public static Polygon convexHull(Area area)
	{
		if (area == null)
			return null;

		Point[] P = convertArea(area);
		if (P.length > 1)
		{
			int n = P.length, k = 0;
			Point[] H = new Point[2 * n];

			Arrays.sort(P);

			// Build lower hull
			for (int i = 0; i < n; ++i)
			{
				while (k >= 2 && cross(H[k - 2], H[k - 1], P[i]) <= 0)
					k--;
				H[k++] = P[i];
			}

			// Build upper hull
			for (int i = n - 2, t = k + 1; i >= 0; i--)
			{
				while (k >= t && cross(H[k - 2], H[k - 1], P[i]) <= 0)
					k--;
				H[k++] = P[i];
			}
			if (k > 1)
			{
				H = Arrays.copyOfRange(H, 0, k - 1); // remove non-hull vertices
														// after k; remove k - 1
														// which is a duplicate
			}

			return convertPoints(H);
		}
		else if (P.length <= 1)
		{
			return convertPoints(P);
		}
		else
		{
			return null;
		}
	}

}