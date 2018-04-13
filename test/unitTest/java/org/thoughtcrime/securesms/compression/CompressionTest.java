package org.thoughtcrime.securesms.compression;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.BaseUnitTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.thoughtcrime.securesms.mms.MediaConstraints;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SiliCompressor.class)
public class  CompressionTest extends BaseUnitTest {

    SiliCompressor mockCompressor;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        mockCompressor = PowerMockito.mock(SiliCompressor.class);
        PowerMockito.when(mockCompressor.compress(anyString(), anyObject())).thenReturn("test/unitTest/java/org/thoughtcrime/securesms/resources/test-img-compressed.jpg");
        PowerMockito.when(mockCompressor.compressVideo(anyString(), anyString())).thenReturn("test/unitTest/java/org/thoughtcrime/securesms/resources/test-video-compressed.mp4");
        PowerMockito.whenNew(SiliCompressor.class).withAnyArguments().thenReturn(mockCompressor);
    }

    @Test
    public void testCompressImageFile() throws Exception {
        File testImg = new File("test/unitTest/java/org/thoughtcrime/securesms/resources/test-img.jpg");
        String testDirectory = testImg.getPath();

        MediaConstraints temp = MediaConstraints.getPushMediaConstraints();

        String compressedPath = temp.compressImage(context, testDirectory);

        Long uncompressedLength = testImg.length();
        Long compressedLength = new File(compressedPath).length();

        assertTrue(compressedLength < uncompressedLength);
    }

    @Test
    public void testCompressVideo() throws Exception {
        Context mockContext = mock(Context.class);
        when(mockContext.getCacheDir()).thenReturn(mock(File.class));

        File testVideo = new File("test/unitTest/java/org/thoughtcrime/securesms/resources/test-video.mp4");
        String testDirectory = testVideo.getPath();

        MediaConstraints temp = MediaConstraints.getPushMediaConstraints();

        String compressedPath = temp.compressVideo(mockContext, testDirectory);

        Long uncompressedLength = testVideo.length();
        Long compressedLength = new File(compressedPath).length();

        assertTrue(compressedLength < uncompressedLength);
    }
}
