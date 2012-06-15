/**
 * 此文件的版权属于<a href="mailto:kris1987@qq.com">Kris.lee</a>.借用一下，略做修改，希望谅解。
 */

package com.senseforce.skintest.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import android.content.Context;

public class ZipUtil {
	private ZipFile         zipFile; 
    private ZipOutputStream zipOut;     //压缩Zip 
    private  int      		bufSize;    //size of bytes 
    private byte[]          buf; 
    private int             readedBytes; 
    public ZipUtil(){ 
        this(512); 
    } 

    public ZipUtil(int bufSize){ 
        this.bufSize = bufSize; 
        this.buf = new byte[this.bufSize]; 
    } 
     
	/**
	 * 
	 * @param srcFile  需要　压缩的目录或者文件
	 * @param destFile　压缩文件的路径
	 */
	public void doZip(String srcFile, String destFile) {// zipDirectoryPath:需要压缩的文件夹名
		File zipDir;
		String dirName;

		zipDir = new File(srcFile);
		dirName = zipDir.getName();
		try {
			this.zipOut = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(destFile)));
			//设置压缩的注释
			zipOut.setComment("comment");
			//设置压缩的编码，如果要压缩的路径中有中文，就用下面的编码
			zipOut.setEncoding("GBK");
			//启用压缩 
			zipOut.setMethod(ZipOutputStream.DEFLATED); 

			//压缩级别为最强压缩，但时间要花得多一点 
			zipOut.setLevel(Deflater.BEST_COMPRESSION); 
			
			handleDir(zipDir, this.zipOut,dirName);
			this.zipOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 *  由doZip调用,递归完成目录文件读取
	 * @param dir
	 * @param zipOut
	 * @param dirName  这个主要是用来记录压缩文件的一个目录层次结构的
	 * @throws IOException
	 */
	private void handleDir(File dir, ZipOutputStream zipOut,String dirName) throws IOException {
		System.out.println("遍历目录："+dir.getName());
		FileInputStream fileIn;
		File[] files;

		files = dir.listFiles();

		if (files.length == 0) {// 如果目录为空,则单独创建之.
			// ZipEntry的isDirectory()方法中,目录以"/"结尾.
			System.out.println("压缩的　Name:"+dirName);
			this.zipOut.putNextEntry(new ZipEntry(dirName));
			this.zipOut.closeEntry();
		} else {// 如果目录不为空,则分别处理目录和文件.
			for (File fileName : files) {
				// System.out.println(fileName);

				if (fileName.isDirectory()) {
					handleDir(fileName, this.zipOut,dirName+File.separator+fileName.getName()+File.separator);
				} else {
					System.out.println("压缩的　Name:"+dirName + File.separator+fileName.getName());
					fileIn = new FileInputStream(fileName);
					this.zipOut.putNextEntry(new ZipEntry(dirName + File.separator+fileName.getName()));

					while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
						this.zipOut.write(this.buf, 0, this.readedBytes);
					}

					this.zipOut.closeEntry();
				}
			}
		}
	}

	/**
	 * 解压指定zip文件
	 * @param unZipfile 压缩文件的路径
	 * @param destFile　　　解压到的目录　
	 */
	public void unZip(String unZipfile, String destFile) {// unZipfileName需要解压的zip文件名
		FileOutputStream fileOut;
		File file;
		InputStream inputStream;

		try {
			this.zipFile = new ZipFile(unZipfile);

			for (Enumeration entries = this.zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				file = new File(destFile+File.separator+entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}

					inputStream = zipFile.getInputStream(entry);

					fileOut = new FileOutputStream(file);
					while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
						fileOut.write(this.buf, 0, this.readedBytes);
					}
					fileOut.close();

					inputStream.close();
				}
			}
			this.zipFile.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// 设置缓冲区大小
	public void setBufSize(int bufSize) {
		this.bufSize = bufSize;
	}
	
	/**
	 * 将另一个apk包中的指定zip文件解压到程序的data目录下
	 * @param originContext	另一个apk的context
	 * @param originAssetFilePath zip文件名
	 * @param destDirectoryPath 解压后文件的保存路径
	 * @return
	 */
	public boolean unZip(Context originContext, String originAssetFilePath, String destDirectoryPath) {
		boolean isSuccessful = false;
		try{
			
			InputStream inputStream = originContext.getResources().getAssets().open(originAssetFilePath);
			String tempFilename = "/sdcard/" + originContext.getPackageName() + "_skin_temp.zip";
			FileOutputStream tempFileOutputStream = new FileOutputStream(new File(tempFilename));
			int readed = 0;
			byte[] buffer = new byte[512];
			while ((readed = inputStream.read(buffer)) > 0) {
				tempFileOutputStream.write(buffer, 0, readed);
			}
			tempFileOutputStream.close();
			inputStream.close();
			
			unZip(tempFilename, destDirectoryPath);
			isSuccessful = true;
			
			new File(tempFilename).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccessful;
	}
}
