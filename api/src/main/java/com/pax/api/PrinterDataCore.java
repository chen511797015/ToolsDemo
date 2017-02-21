package com.pax.api;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chendd on 2017/2/14.
 */

public class PrinterDataCore {

    private static final String TAG = "PrinterDataCore";
    public int BitmapWidth = 0;
    public int PrintDataHeight = 0;//打印的高度
    public byte HalftoneMode = 1;//半色调模式
    public byte ScaleMode = 0;//缩放模式
    public byte CompressMode = 0;//压缩模式
    private int v = 0;
    private int w = 0;

    public PrinterDataCore() {
    }


    public byte[] PrintDataFormat(Bitmap var1) {
        try {
            byte[] var3;
            if (this.HalftoneMode > 0) {
                var3 = this.a(var1);
            } else {
                var3 = this.b(var1);
            }

            return this.b(var3);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }


    private byte[] b(byte[] var1) {
        Object var2;
        try {
            int var10000 = this.BitmapWidth;
            var2 = null;
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            ArrayList var5 = new ArrayList();
            boolean var6 = false;
            boolean var7 = false;

            int var8;
            for (var8 = 0; var8 < this.PrintDataHeight; ++var8) {
                boolean var9 = true;
                boolean var10 = false;
                int var15 = 0;
                int var16 = 0;
                int var11 = var8 * this.BitmapWidth;
                byte[] var14 = new byte[this.BitmapWidth];

                int var12;
                for (var12 = 0; var12 < this.BitmapWidth; ++var12) {
                    byte var17;
                    if ((var17 = var1[var11 + var12]) != 0) {
                        if (var12 == 0) {
                            var15 = 0;
                        } else if (var15 > var16) {
                            var15 = var16;
                        }

                        var16 = var12;
                        var9 = false;
                    }

                    var14[var12] = var17;
                }

                if (var9) {
                    var5.add(var14);
                } else {
                    int var10001;
                    PrinterDataCore var18;
                    label77:
                    {
                        if (this.v == 0) {
                            var18 = this;
                        } else {
                            var18 = this;
                            if (this.v < var15) {
                                var10001 = this.v;
                                break label77;
                            }
                        }

                        var10001 = var15;
                    }

                    var18.v = var10001;
                    this.w = this.w < var16 ? var16 : this.w;
                    if ((var12 = var5.size()) > 0) {
                        if (var12 > 24) {
                            if (var4.size() > 0) {
                                var3.add(this.b((List) var4));
                            }

                            var3.add(this.a((List) var5));
                            var4 = new ArrayList();
                        } else {
                            var4.addAll(var5);
                        }

                        var5 = new ArrayList();
                    }

                    var4.add(var14);
                }
            }

            if ((var8 = var5.size()) > 0) {
                if (var8 > 24) {
                    if (var4.size() > 0) {
                        var3.add(this.b((List) var4));
                    }

                    var3.add(this.a((List) var5));
                } else {
                    var4.addAll(var5);
                    var3.add(this.b((List) var4));
                }
            } else {
                var3.add(this.b((List) var4));
            }

            return this.sysCopy(var3);
        } catch (Exception var13) {
            var2 = null;
            var13.printStackTrace();
            return null;
        }
    }

    private byte[] a(List var1) {
        try {
            ArrayList var2 = new ArrayList();
            int var3 = 0;

            for (int var4 = var1.size(); var3 < var4; var3 += 240) {
                byte[] var6 = new byte[]{(byte) 27, (byte) 74, (byte) 0};
                if (var4 - var3 > 240) {
                    var6[2] = -16;
                } else {
                    var6[2] = (byte) (var4 - var3);
                }
                var2.add(var6);
            }
            return this.sysCopy(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    private byte[] b(List var1) {
        try {
            int var2 = this.w - this.v + 1;
            int var3 = 0;
            int var4 = var1.size();
            int var5 = 0;
            int var6 = var4 % 2300 > 0 ? var4 / 2300 + 1 : var4 / 2300;
            byte[] var10;
            for (var10 = new byte[var2 * var4 + 8 * var6]; var3 < var4; ++var5) {
                int var7 = var3 + 2300 < var4 ? 2300 : var4 - var3;
                int var8 = 2308 * var5;
                var10[var8] = 29;
                var10[var8 + 1] = 118;
                var10[var8 + 2] = 48;
                var10[var8 + 3] = this.ScaleMode;
                var10[var8 + 4] = (byte) (var2 % 256);
                var10[var8 + 5] = (byte) (var2 / 256);
                var10[var8 + 6] = (byte) (var7 % 256);
                var10[var8 + 7] = (byte) (var7 / 256);
                for (var7 = var3; var7 < var4; ++var7) {
                    System.arraycopy(var1.get(var7), this.v, var10, 2308 * var5 + var7 * var2 + 8, var2);
                }
                var3 += 2300;
            }
            this.v = 0;
            this.w = 0;
            return var10;
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    private byte[] a(Bitmap var1) {
        int var2 = var1.getWidth();
        int var3 = var1.getHeight();
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        boolean var7 = false;
        int var8 = 0;
        boolean var9 = false;
        int var10 = var2 + 7 >> 3;
        Object var11 = null;
        try {
            this.PrintDataHeight = var3;
            this.BitmapWidth = var10;
            int var16 = var2 * var3;
            int var18 = var10 * var3;
            int[] var21 = new int[var16];
            var1.getPixels(var21, 0, var2, 0, 0, var2, var3);

            int var17;
            for (var17 = 0; var17 < var16; ++var17) {
                int var13 = var21[var17];
                var21[var8++] = 255 & (byte) ((int) ((double) Color.red(var13) * 0.29891D + (double) Color.green(var13) * 0.58661D + (double) Color.blue(var13) * 0.11448D));
            }
            for (var17 = 0; var17 < var3; ++var17) {
                var8 = var17 * var2;
                for (var16 = 0; var16 < var2; ++var16) {
                    float var14;
                    if (var21[var8] > 128) {
                        var14 = (float) (var21[var8] - 255);
                        var21[var8] = 255;
                    } else {
                        var14 = (float) (var21[var8] - 0);
                        var21[var8] = 0;
                    }
                    if (var16 < var2 - 1) {
                        var21[var8 + 1] += (int) (0.4375D * (double) var14);
                    }
                    if (var17 < var3 - 1) {
                        if (var16 > 1) {
                            var21[var8 + var2 - 1] += (int) (0.1875D * (double) var14);
                        }
                        var21[var8 + var2] += (int) (0.3125D * (double) var14);
                        if (var16 < var2 - 1) {
                            var21[var8 + var2 + 1] += (int) (0.0625D * (double) var14);
                        }
                    }
                    ++var8;
                }
            }

            byte[] var15 = new byte[var18];

            label64:
            for (var17 = 0; var17 < var3; ++var17) {
                var8 = var17 * var2;
                int var20 = var17 * var10;
                var18 = 0;
                var16 = 0;
                while (true) {
                    int var19;
                    do {
                        if (var16 >= var2) {
                            continue label64;
                        }
                        var19 = var16 % 8;
                        if (var21[var8++] <= 128) {
                            var18 |= 128 >> var19;
                        }
                        ++var16;
                    } while (var19 != 7 && var16 != var2);
                    var15[var20++] = (byte) var18;
                    var18 = 0;
                }
            }
            return var15;
        } catch (Exception var12) {
            var12.printStackTrace();
            return null;
        }
    }

    private byte[] b(Bitmap var1) {
        boolean var7 = false;
        boolean var8 = false;
        boolean var6 = false;
        int var9 = 0;
        int var10 = 0;
        boolean var11 = false;

        try {
            int var2 = var1.getWidth();
            int var3 = var1.getHeight();
            this.PrintDataHeight = var3;
            int var4 = var2 % 8 == 0 ? var2 : (var2 / 8 + 1) * 8;
            this.BitmapWidth = var4 / 8;
            byte[] var5 = new byte[var4 = var3 * this.BitmapWidth];
            for (int var12 = 0; var12 < var4; ++var12) {
                var5[var12] = 0;
            }
            while (var9 < var3) {
                int[] var19 = new int[var2];
                var1.getPixels(var19, 0, var2, 0, var9, var2, 1);
                int var18 = 0;
                for (int var13 = 0; var13 < var2; ++var13) {
                    ++var18;
                    int var15 = var19[var13];
                    if (var18 > 8) {
                        var18 = 1;
                        ++var10;
                    }
                    if (var15 != -1) {
                        var4 = 1 << 8 - var18;
                        int var16 = Color.red(var15);
                        int var17 = Color.green(var15);
                        var15 = Color.blue(var15);
                        if ((var16 + var17 + var15) / 3 < 128) {
                            var5[var10] = (byte) (var5[var10] | var4);
                        }
                    }
                }
                var10 = this.BitmapWidth * (var9 + 1);
                ++var9;
            }
            return var5;
        } catch (Exception var14) {
            var14.printStackTrace();
            return null;
        }
    }

    public byte[] sysCopy(List var1) {
        int var2 = 0;
        byte[] var3;
        for (Iterator var4 = var1.iterator(); var4.hasNext(); var2 += var3.length) {
            var3 = (byte[]) var4.next();
        }
        var3 = new byte[var2];
        int var7 = 0;
        byte[] var5;
        for (Iterator var6 = var1.iterator(); var6.hasNext(); var7 += var5.length) {
            System.arraycopy(var5 = (byte[]) var6.next(), 0, var3, var7, var5.length);
        }
        return var3;
    }
}
