package org.thoughtcrime.securesms.compression;
import android.content.Context;
import android.net.Uri;

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
import org.thoughtcrime.securesms.attachments.DatabaseAttachment;
import org.thoughtcrime.securesms.mms.MediaConstraints;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SiliCompressor.class)
public class  CompressionTest extends BaseUnitTest {

    SiliCompressor mockCompressor;

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

    @Test
    public void testSatisfiesCompressionSetting() throws Exception {
        Attachment a1 = getMockAttachment("image/jpeg");
        Attachment a2 = getMockAttachment("image/gif");
        Attachment a3 = getMockAttachment("video/mp4");

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
        Attachment a1 = getMockAttachment("image/jpeg");
        Attachment a2 = getMockAttachment("image/gif");
        Attachment a3 = getMockAttachment("video/mp4");

        Set<String> testPreferences = new HashSet<String>();

        MediaConstraints temp = MediaConstraints.getPushMediaConstraints();

        assertTrue(!temp.satisfiesCompression(context, a1, testPreferences));
        assertTrue(!temp.satisfiesCompression(context, a2, testPreferences));
        assertTrue(!temp.satisfiesCompression(context, a3, testPreferences));
    }
}
