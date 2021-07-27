package com.progwml6.ironchest.client.model.inventory;

import net.minecraft.util.math.vector.Vector3f;

public class ModelItem {
  private final Vector3f center;
  private final float size;

  /** Item center location in percentages, lazy loaded */
  private Vector3f centerScaled;
  /** Item size in percentages, lazy loaded */
  private Float sizeScaled;

  public ModelItem(Vector3f center, float size) {
    this.center = center;
    this.size = size;
  }

  /**
   * Gets the center for rendering this item, scaled for renderer
   * @return Scaled center
   */
  public Vector3f getCenterScaled() {
    if (centerScaled == null) {
      centerScaled = center.copy();
      centerScaled.mul(1f / 16f);
    }

    return centerScaled;
  }

  /**
   * Gets the size to render this item, scaled for the renderer
   * @return Size scaled
   */
  public float getSizeScaled() {
    if (sizeScaled == null) {
      sizeScaled = size / 16f;
    }

    return sizeScaled;
  }

  /**
   * Gets the center for rendering this item
   * @return Center
   */
  public Vector3f getCenter() {
    return center;
  }

  /**
   * Gets the size to render this item
   * @return Size
   */
  public float getSize() {
    return size;
  }
}
