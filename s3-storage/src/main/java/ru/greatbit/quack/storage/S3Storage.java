package ru.greatbit.quack.storage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ru.greatbit.quack.beans.Attachment;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class S3Storage implements Storage {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${storage.amazons3.accesskey}")
    private String AMAZON_STORAGE_ACCESS_KEY;

    @Value("${storage.amazons3.secretkey}")
    private String AMAZON_STORAGE_SECRET_KEY;

    @Value("${storage.amazons3.bucketName}")
    private String AMAZON_STORAGE_BUCKET_NAME; // Bucket name should not contain upper case chars and '_'

    @Value("${storage.amazons3.region}")
    private String AMAZON_STORAGE_REGION;

    @Value("${storage.amazons3.timeout}")
    private int AMAZON_STORAGE_TIMEOUT;

    @Value("${storage.amazons3.max.retry}")
    private int AMAZON_STORAGE_RETRIES;


    private AmazonS3 client;
    private Bucket bucket;
    private String AMAZON_FILE_PATH_FLAG = "amazonS3://";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_dd_yyyy_hh_mm_");

    @PostConstruct
    public void init() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(AMAZON_STORAGE_ACCESS_KEY, AMAZON_STORAGE_SECRET_KEY);
        ClientConfiguration configuration = new ClientConfiguration()
                .withConnectionTimeout(AMAZON_STORAGE_TIMEOUT)
                .withSocketTimeout(AMAZON_STORAGE_TIMEOUT)
                .withTcpKeepAlive(true)
                .withMaxErrorRetry(AMAZON_STORAGE_RETRIES);
        client = new AmazonS3Client(awsCreds, configuration);

        if (!isBucketExists()) {
            try {
                bucket = client.createBucket(AMAZON_STORAGE_BUCKET_NAME);
            } catch (AmazonServiceException e) {
                logger.error(e.getErrorMessage());
                throw e;
            }
        }
    }

    @Override
    public Attachment upload(InputStream uploadedInputStream, String fileName, long size) throws IOException {
        String fileNameInCloud = LocalDateTime.now().format(formatter) + fileName;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            client.putObject(new PutObjectRequest(AMAZON_STORAGE_BUCKET_NAME, fileNameInCloud,
                    uploadedInputStream, metadata));
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            throw new IOException(e);
        }
        return new Attachment().withUrl(AMAZON_FILE_PATH_FLAG + fileNameInCloud).withDataSize(size);
    }

    @Override
    public void remove(Attachment attachment) throws IOException {
        try {
            client.deleteObject(AMAZON_STORAGE_BUCKET_NAME,
                    attachment.getUrl().replace(AMAZON_FILE_PATH_FLAG, ""));
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
        }
    }

    @Override
    public InputStream get(Attachment attachment) throws IOException {
        byte[] content = null;
        try {
            S3Object o = client.getObject(AMAZON_STORAGE_BUCKET_NAME,
                    attachment.getUrl().replace(AMAZON_FILE_PATH_FLAG, ""));
            S3ObjectInputStream s3is = o.getObjectContent();
            content = IOUtils.toByteArray(s3is);
            s3is.close();
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            throw new IOException(e);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ByteArrayInputStream(content);
    }



    private boolean isBucketExists() {
        List<Bucket> buckets = client.listBuckets();
        boolean result = buckets.stream().anyMatch(bucket -> bucket.getName().equals(AMAZON_STORAGE_BUCKET_NAME));
        return result;
    }
}
