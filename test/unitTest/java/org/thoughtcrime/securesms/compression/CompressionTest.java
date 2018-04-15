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

import org.thoughtcrime.securesms.attachments.Attachment;
import org.thoughtcrime.securesms.mms.MediaConstraints;
import org.thoughtcrime.securesms.util.MediaUtil;

import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SiliCompressor.class)
public class  CompressionTest extends BaseUnitTest {
    private static String TEET_IMG_FILE = "test/unitTest/java/org/thoughtcrime/securesms/resources/test-img.jpg";
    private static String TEST_VIDEO_FILE = "test/unitTest/java/org/thoughtcrime/securesms/resources/test-video.mp4";
    private static String COMPRESSED_TEST_IMG_FILE = "test/unitTest/java/org/thoughtcrime/securesms/resources/test-img-compressed.jpg";
    private static String COMPRESSED_TEST_VIDEO_FILE = "test/unitTest/java/org/thoughtcrime/securesms/resources/test-video-compressed.mp4";

    private SiliCompressor mockCompressor;

    private Attachment getMockAttachment(String contentType) {
        Attachment attachment = mock(Attachment.class);
        when(attachment.getContentType()).thenReturn(contentType);

        return attachment;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        mockCompressor = PowerMockito.mock(SiliCompressor.class);
        PowerMockito.when(mockCompressor.compress(anyString(), anyObject())).thenReturn(COMPRESSED_TEST_IMG_FILE);
        PowerMockito.when(mockCompressor.compressVideo(anyString(), anyString())).thenReturn(COMPRESSED_TEST_VIDEO_FILE);
        PowerMockito.whenNew(SiliCompressor.class).withAnyArguments().thenReturn(mockCompressor);
    }

    @Test
    public void testCompressImageFile() throws Exception {
        File testImg = new File(TEET_IMG_FILE);
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

        File testVideo = new File(TEST_VIDEO_FILE);
        String testDirectory = testVideo.getPath();

        MediaConstraints temp = MediaConstraints.getPushMediaConstraints();

        String compressedPath = temp.compressVideo(mockContext, testDirectory);

        Long uncompressedLength = testVideo.length();
        Long compressedLength = new File(compressedPath).length();

        assertTrue(compressedLength < uncompressedLength);
    }

    @Test
    public void testSatisfiesCompressionSetting() throws Exception {
        Attachment a1 = getMockAttachment(MediaUtil.IMAGE_JPEG);
        Attachment a2 = getMockAttachment(MediaUtil.IMAGE_GIF);
        Attachment a3 = getMockAttachment(MediaUtil.VIDEO_MP4);

        Set<String> testPreferences = new HashSet<String>();
        testPreferences.add("image");
        testPreferences.add("video");
        testPreferences.add("gif");

        MediaConstraints temp = MediaConstraints.getPushMediaConstraints();

        assertTrue(temp.satisfiesCompression(context, a1, testPreferences));
        assertTrue(temp.satisfiesCompression(context, a2, testPreferences));
        assertTrue(temp.satisfiesCompression(context, a3, testPreferences));
    }

    @Test
    public void testDoesntSatisfyCompressionSetting() throws Exception {
        Attachment a1 = getMockAttachment(MediaUtil.IMAGE_JPEG);
        Attachment a2 = getMockAttachment(MediaUtil.IMAGE_GIF);
        Attachment a3 = getMockAttachment(MediaUtil.VIDEO_MP4);

        Set<String> testPreferences = new HashSet<String>();

        MediaConstraints temp = MediaConstraints.getPushMediaConstraints();

        assertFalse(temp.satisfiesCompression(context, a1, testPreferences));
        assertFalse(temp.satisfiesCompression(context, a2, testPreferences));
        assertFalse(temp.satisfiesCompression(context, a3, testPreferences));
    }
}
