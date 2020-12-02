/* 
 -----------------------------------------------------------
 An image-denoiser using Java.

 (C) 2020 Sandra VS Nair, Trivandrum
 email sandravsnair@gmail.com
 -----------------------------------------------------------
*/

package image_denoise;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays; 

public class ImageTransformer {

	//Variables to store original image and denoised image.
	static Image img;
	static BufferedImage denoised_img;
	
	//Method to sort an array and find the median.
	public static int findMedian(int arr[], int n) 
    { 
        // Sort the array 
        Arrays.sort(arr); 
  
        // For even number of elements
        if (n % 2 != 0) 
        return (int)arr[n / 2]; 
      
        // For odd number of elements
        return (int)(arr[(n - 1) / 2] + arr[n / 2]) / 2; 
    } 
    
	//Method to find the neighbors of a 2D Matrix (Image) element.
	public static int[] findNeighbors(int row, int col) 
    { 
		//Get original width and height of the image.
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		
		//Arrays to store the red,green and blue values of each neighbor element.
        int red[] = new int[9];
        int green[] = new int[9];
        int blue[] = new int[9];
        
        int pixel;
        int i = 0;
        int x,y;
        
        //For each neighbor element (including the current element), get its red, blue and green value and append to respective arrays.
        for(x = row - 1;x <= row+1;x++) {
        	if(x < height && x >= 0) {
        		for(y = col - 1;y <= col+1;y++) {
        			if (y < width && y >= 0) {
        				pixel = ((BufferedImage) img).getRGB(y,x);
        				red[i] = (pixel >> 16) & 0xFF;
        				green[i] = (pixel >> 8) & 0xFF;
        				blue[i] = pixel & 0xFF;
        				i++;
        			}
        		}
        	}
        }
        
        //Find median of each array.
        int redclr = findMedian(red,red.length);
        int greenclr = findMedian(green,green.length);
        int blueclr = findMedian(blue,blue.length);
        
        //Store median values in an array and return it.
        int color[] = {redclr,greenclr,blueclr};
        return color;
    } 
	
	//Function to denoise an image.
	public static void denoise() {
		//Get original width and height of the image.
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		
		int pixel;
		int a,p;
		
		//Initialize a BufferedImage with the same width and height.
		denoised_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		int color_array[] = new int[3];
		
		//For each element in the original image, denoise it by finding the median of it and its neighbors.
		//Set the median values as the RGB value of denoised_img.
		
		for(int col = 0; col < width; col++) {
			for(int row = 0; row < height; row++) {
				pixel = ((BufferedImage) img).getRGB(col,row);
				color_array=findNeighbors(row,col);
				a = (pixel >>24) & 0xFF;
				p = (a<<24) | (color_array[0]<<16) | (color_array[1]<<8) | color_array[2]; 
		        denoised_img.setRGB(col, row, p);			}
		}
	}
	
	//Execution starts here
	public static void main(String[] args) {
		
		//Read original image
		try {
            img = ImageIO.read(new File("D:\\photo.jpg"));
            denoise();		//Calling denoise() function.
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		//Write the denoised image 
		File f = null;
		
        try
        { 
            f = new File("D:\\Out.jpg"); 
            ImageIO.write(denoised_img, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }

	}

}
