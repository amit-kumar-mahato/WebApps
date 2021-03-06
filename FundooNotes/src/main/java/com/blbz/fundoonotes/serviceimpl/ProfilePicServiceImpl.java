package com.blbz.fundoonotes.serviceimpl;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.blbz.fundoonotes.model.Profile;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.ProfilePicRepository;
import com.blbz.fundoonotes.repository.UserRepository;
import com.blbz.fundoonotes.service.ProfileService;
import com.blbz.fundoonotes.utility.JwtGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfilePicServiceImpl implements ProfileService {

	@Autowired
	private ProfilePicRepository profilePicRepository;

	@Autowired
	private UserRepository userRepository;

	@Value("${aws.bucket.name}")
	private String bucketName;

	@Autowired
	private AmazonS3 amazonS3Client;

	@Autowired
	private JwtGenerator jwtGenerator;

	public Profile storeObjectInS3(MultipartFile file, String fileName, String contentType, String token) {
		try {
			long userId = jwtGenerator.parseJWT(token);
			User user = userRepository.findOneByUserId(userId);
			if (user!=null) {
				Profile profile = new Profile(fileName, user);
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(contentType);
				objectMetadata.setContentLength(file.getSize());

				amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
				profilePicRepository.save(profile);
				return profile;
			}
		} catch (AmazonClientException | IOException exception) {
			throw new RuntimeException("Error while uploading file.");
		}
		return null;

	}

	public S3Object fetchObject(String awsFileName) {
		S3Object s3Object;
		try {
			s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, awsFileName));
		} catch (AmazonServiceException serviceException) {

			throw new RuntimeException("Error while streaming File.");
		} catch (AmazonClientException exception) {

			throw new RuntimeException("Error while streaming File.");
		}
		return s3Object;
	}

	public void deleteObject(String key) {
		try {
			amazonS3Client.deleteObject(bucketName, key);
		} catch (AmazonServiceException serviceException) {
			log.error(serviceException.getErrorMessage());
		} catch (AmazonClientException exception) {
			log.error("Error while deleting File.");
		}
	}

	@Override
	public Profile updateObejctInS3(MultipartFile file, String originalFilename, String contentType, String token) {
		try {
			long userId = jwtGenerator.parseJWT(token);
			Optional<User> user = userRepository.findById(userId);
			if (user.isPresent()) {
				Profile profile = profilePicRepository.findByUserId(userId);
				if (profile != null) {
					deleteObject(profile.getProfilePicName());
					profilePicRepository.delete(profile);
					ObjectMetadata objectMetadata = new ObjectMetadata();
					objectMetadata.setContentType(contentType);
					objectMetadata.setContentLength(file.getSize());

					amazonS3Client.putObject(bucketName, originalFilename, file.getInputStream(), objectMetadata);
					profilePicRepository.save(profile);
					return profile;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public S3Object getProfilePic(String token) {

		try {
			long userId = jwtGenerator.parseJWT(token);
			Optional<User> user = userRepository.findById(userId);
			if (user.isPresent()) {
				Profile profile = profilePicRepository.findByUserId(userId);
				if (profile != null) {
					return fetchObject(profile.getProfilePicName());
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}