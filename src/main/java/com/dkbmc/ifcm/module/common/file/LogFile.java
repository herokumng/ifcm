package com.dkbmc.ifcm.module.common.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileWriteType;
import com.dkbmc.ifcm.module.core.ModuleConst;

/**
 *
 * @author DY
 *
 */
public class LogFile extends FileHandler {
	// Write Log File
	private AsynchronousFileChannel writeChannel;
	private ExecutorService executorService;
	private static final double ASYNC_THREADS_RATIO = 0.5; // fixed option

	private static final int THREADS = (int) Math
			.round(Runtime.getRuntime().availableProcessors() * ASYNC_THREADS_RATIO);

	// Read Log File
	private RandomAccessFile randomAccessFile;
	private FileChannel readChannel;
	private ByteBuffer readBuffer;
	private static final int READ_BUFFER_SIZE = 10 * 1024; // 10 K fixed option
	private long readPosition;

	private void openWriteChannel() throws IOException {
		executorService = Executors.newFixedThreadPool(THREADS);
		writeChannel = AsynchronousFileChannel.open(Paths.get(getFilePath()), EnumSet.of(StandardOpenOption.WRITE),
				executorService);
	}

	private void openReadChannel() throws IOException {
		randomAccessFile = new RandomAccessFile(getFile(), "r"); // fiexed option
		readChannel = randomAccessFile.getChannel();
		readBuffer = ByteBuffer.allocateDirect(READ_BUFFER_SIZE);
		readPosition = ModuleConst.MODULE_DIGIT_0;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public <T> T read(FileContentType file_cont_type, FileReadType file_read_type) {
		// TODO Auto-generated method stub
		String readValue = null;

		try {
			if (readChannel == null || !readChannel.isOpen()) {
				openReadChannel();
			}

			if (file_read_type.equals(FileReadType.BUFFER)) {
				final byte LINE_FEED = 0x0A; // 개행 Hex Code
				randomAccessFile.seek(readPosition);
				long readBytes;
				if ((readBytes = readChannel.read(readBuffer)) >= ModuleConst.MODULE_DIGIT_0) {
					int newLinePosition = readBuffer.position();
					while (newLinePosition > ModuleConst.MODULE_DIGIT_0) {
						if (readBuffer.get(--newLinePosition) == LINE_FEED) {
							readBuffer.position(ModuleConst.MODULE_DIGIT_0);
							readBuffer.limit(++newLinePosition);
							readValue = StandardCharsets.UTF_8.decode(readBuffer).toString();
							readPosition += readBuffer.limit();
							readBuffer.clear();
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T) readValue;
	}

	@Override
	public void write(String output_content) {
		// TODO Auto-generated method stub
		try {
			if (writeChannel == null || !writeChannel.isOpen()) {
				openWriteChannel();
			}

			AtomicLong position = new AtomicLong(ModuleConst.MODULE_DIGIT_0);
			position.set(writeChannel.size()); // end of log file

			String logMessage = output_content + System.lineSeparator();
			ByteBuffer buffer = ByteBuffer.allocateDirect(logMessage.length());

			buffer.put(logMessage.getBytes(StandardCharsets.UTF_8)).flip();

			long writePosition = position.getAndAdd(logMessage.length());

			// 콜백 메소드에서 결과값 이외에 추가작업을 위한 매개체
			class Attachment {
				AsynchronousFileChannel filechannel;
			}
			Attachment attachment = new Attachment();
			attachment.filechannel = writeChannel;

			// Call Back Handler
			CompletionHandler<Integer, Attachment> completionHander = new CompletionHandler<>() {
				@Override
				public void completed(Integer result, Attachment attachment) {
					try { // 정상 종료 시 호출
						attachment.filechannel.close();
					} catch (IOException e) {
					}
				}

				@Override
				public void failed(Throwable exception, Attachment attachment) {
					exception.printStackTrace();
					try {
						attachment.filechannel.close();
					} catch (IOException e) {
					}
				}
			};

			writeChannel.write(buffer, writePosition, attachment, completionHander);
			// channel.wait();
			executorService.isShutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public <T> boolean write(FileContentType file_cont_type, FileWriteType file_write_type, T output_content,
			int line_number) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(FileUseType use_Type) {
		// TODO Auto-generated method stub
		try {
			if (use_Type.equals(FileUseType.READ) && readChannel != null) {
				readBuffer.clear();
				readChannel.close();
				randomAccessFile.close();
			}
			if (use_Type.equals(FileUseType.WRITE) && writeChannel != null) {
				writeChannel.close();
				executorService.isTerminated();
			}
			closed = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void copy(String source_path, String target_path) {
		// TODO Auto-generated method stub
	}

	@Override
	public <T> boolean delete(String file_path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void download(String filePath, String fileName) {

	}

}
