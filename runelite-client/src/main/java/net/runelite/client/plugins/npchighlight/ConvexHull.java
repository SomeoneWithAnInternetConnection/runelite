/*
 * Copyright (c) 2018, James Swindle <wilingua@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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