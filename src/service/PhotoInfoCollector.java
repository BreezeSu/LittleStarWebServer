package service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import common.Constant;
import model.PhotoInfoRestModel;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片信息收集类
 *
 * @author Su
 *
 */
public class PhotoInfoCollector
{

	private final Logger log = Logger.getLogger(PhotoInfoCollector.class);

	/**
	 * 单例
	 */
	private static PhotoInfoCollector INSTANCE;

	private PhotoInfoCollector()
	{

	}

	/**
	 * 获取单例的方法
	 *
	 * @return 单例
	 */
	public static PhotoInfoCollector getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new PhotoInfoCollector();
		}
		return INSTANCE;
	}

	private final List<PhotoInfoRestModel> photoInfoList = new ArrayList<PhotoInfoRestModel>();

	/**
	 * 读取图片信息
	 *
	 * @return 图片信息列表
	 */
	public List<PhotoInfoRestModel> collectPhotoInfo()
	{
		final File[] photoFileList = filtPhoto(Constant.PHOTO_DEFAULT_PATH);
		for (final File photoFile : photoFileList)
		{
			photoInfoList.add(getPhotoEXIF(photoFile)); // 根据后缀判断
		}
		return photoInfoList;
	}

	/**
	 * 获取图片EXIF信息
	 *
	 * @param photoFile
	 *            图片的File格式
	 * @return PhotoInfoModel
	 */
	private PhotoInfoRestModel getPhotoEXIF(File photoFile)
	{
		final PhotoInfoRestModel photoInfo = new PhotoInfoRestModel();
		try
		{
			final Metadata metadata = JpegMetadataReader.readMetadata(photoFile);

			for (final Directory directory : metadata.getDirectories())
			{
				if ("ExifSubIFDDirectory".equalsIgnoreCase(directory.getClass().getSimpleName()))
				{
					photoInfo.setName(photoFile.getName());
					photoInfo.setDate(directory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
					photoInfo.setCameraModel(directory.getString(ExifIFD0Directory.TAG_MAKE));
					photoInfo.setxResolution(directory.getString(ExifIFD0Directory.TAG_X_RESOLUTION));
					photoInfo.setyResolution(directory.getString(ExifIFD0Directory.TAG_Y_RESOLUTION));
				}
			}
		}
		catch (final JpegProcessingException e)
		{
			log.error("FileName is : " + photoFile.getName() + "\\n" + e.getMessage());
		}
		catch (final IOException e)
		{
			log.error(e.getStackTrace());
		}
		return photoInfo;
	}

	private File[] filtPhoto(String path)
	{
		final File allFile = new File(path);

		final File[] photoFileList = allFile.listFiles(new PhotoFilter());
		return photoFileList;
	}

	private class PhotoFilter implements FileFilter
	{

		private final String[] acceptedExtensions = new String[] { ".jpg" };

		@Override
		public boolean accept(final File file)
		{
			if (file.isDirectory())
			{
				return false;
			}
			for (final String extension : acceptedExtensions)
			{
				if (file.getName().toLowerCase().endsWith(extension))
				{
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 获取图片的缩略图
	 *
	 * @param photoUrl
	 *            图片的绝对地址
	 * @param outputStr
	 *            生成缩略图的OUT流，这里主要是往httpResponse里写流。
	 */
	public void snapView(String photoUrl, OutputStream outputStr)
	{
		try
		{
			Thumbnails.of(photoUrl).size(200, 300).toOutputStream(outputStr);
		}
		catch (final IOException e)
		{
			log.error("get Snap view of " + photoUrl + " faild.", e);
		}
	}
}
