package service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import model.PhotoInfoRestModel;

public class ResponseManager
{
	private final Logger log = Logger.getLogger(ResponseManager.class);
	private static ResponseManager INSTANCE;

	private ResponseManager()
	{

	}

	public static ResponseManager getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ResponseManager();
		}
		return INSTANCE;
	}

	public HttpServletResponse getJsonInfo(List<PhotoInfoRestModel> photoInfoList, HttpServletResponse response)
	{

		final String typeJson = "application/json; charset=UTF-8";
		final Gson gson = new Gson();
		final String jsonResult = gson.toJson(photoInfoList);
		response.setContentType(typeJson);
		PrintWriter out;
		try
		{
			out = response.getWriter();
			out.print(jsonResult);
			out.close();
		}
		catch (final IOException e)
		{
			log.error(e.getMessage());
		}

		return response;
	}

	public HttpServletResponse getImageInfo(String imagePath, HttpServletResponse response) throws IOException
	{

		final String typeGif = "image/gif;charset=GB2312";// 设定输出的类型

		final String typeJPG = "image/jpeg;charset=GB2312";

		// final String imagePath = "F:\\豆宝的照片\\IMG_9334.JPG";

		response.reset();

		final OutputStream output = response.getOutputStream();// 得到输出流
		if (imagePath.toLowerCase().endsWith(".jpg"))// 使用编码处理文件流的情况：
		{
			response.setContentType(typeJPG);// 设定输出的类型
			// 得到图片的真实路径

			// 得到图片的文件流
			final InputStream imageIn = new FileInputStream(new File(imagePath));
			// 得到输入的编码器，将文件流进行jpg格式编码
			final JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
			// 得到编码后的图片对象
			final BufferedImage image = decoder.decodeAsBufferedImage();
			// 得到输出的编码器
			final JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
			encoder.encode(image);// 对图片进行输出编码
			imageIn.close();// 关闭文件流
		}
		if (imagePath.toLowerCase().endsWith(".gif"))// 不使用编码处理文件流的情况：
		{
			response.setContentType(typeGif);

			// final InputStream imageIn =
			// context.getResourceAsStream(imagePath);// 文件流
			final InputStream imageIn = new FileInputStream(new File(imagePath));
			final BufferedInputStream bis = new BufferedInputStream(imageIn);// 输入缓冲流
			final BufferedOutputStream bos = new BufferedOutputStream(output);// 输出缓冲流
			final byte data[] = new byte[4096];// 缓冲字节数
			int size = 0;
			size = bis.read(data);
			while (size != -1)
			{
				bos.write(data, 0, size);
				size = bis.read(data);
			}
			bis.close();
			bos.flush();// 清空输出缓冲流
			bos.close();
		}
		output.close();
		return response;
	}

}
