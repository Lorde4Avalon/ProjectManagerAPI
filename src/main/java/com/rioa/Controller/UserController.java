package com.rioa.Controller;

import com.rioa.Pojo.Task;
import com.rioa.Pojo.User;
import com.rioa.dao.TaskRepository;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("current_user")
    public User currentUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).get();
        user.setPassword("");
        return user;
    }

    //post user avatar
    @PostMapping("/avatar")
    public String postAvatar(@RequestParam("image") MultipartFile multipartFile,
                             Authentication authentication) throws IOException {
        User user = userRepository.findByUsername(authentication.getName()).get();
//        System.out.println(multipartFile.getContentType());
        if (!Arrays.asList("image/jpeg", "image/png").contains(multipartFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong file type");
        }

        byte[] bytes = compressImage(multipartFile);
        user.setAvatar(bytes);
        userRepository.save(user);
        return user.getUsername() + "add avatar successfully";
    }

    //get user avatar
    @GetMapping("/avatar")
    public byte[] getAvatar(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).get();

        if (user.getAvatar() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avatar not found");
        }
        return user.getAvatar();
    }

    public byte[] compressImage(MultipartFile image) throws IOException
    {

        InputStream inputStream = image.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        float imageQuality = 0.3f;

        // Create the buffered image
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        // Get image writers
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg"); // Input your Format Name here

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = imageWriters.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        // Set the compress quality metrics
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);

        // Compress and insert the image into the byte array.
        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

        byte[] imageBytes = outputStream.toByteArray();

        // close all streams
        inputStream.close();
        outputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();


        return imageBytes;
    }

    @GetMapping("/get/all")
    List<String> getAllUserName() {
        List<String> usernames = new LinkedList<>();
        userRepository.findAll().forEach(user -> {
            usernames.add(user.getUsername());
        });
        return usernames;
    }
}
