package PWC.controllers;
import PWC.entities.Comment;
import PWC.entities.Post;
import PWC.entities.User;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import PWC.services.PostService;
import PWC.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    private static final Logger LOGGER = Logger.getLogger(PostController.class.getName());


    /*
        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Post> createPost(
                @RequestParam("content") String content,
                @RequestParam("image") MultipartFile image
               ) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();
                Post post = new Post();
                post.setUser(user);
                post.setContent(content);
                post.setImage(image.getBytes());
                Post savedPost = postRepository.save(post);
                return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    */@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Post post = new Post();
            post.setUser(user);
            post.setContent(content);
            post.setImage(image.getBytes());
            Post savedPost = postRepository.save(post);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<List<Post>> getAllPostsWithComments() {
        List<Post> posts = postRepository.findAll();


        return ResponseEntity.ok(posts);
    }

    @PostMapping("/post/{postId}/user/{userId}")
    public Comment createComment(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Comment  commentaire) {
        return postService.createComment(postId, userId, commentaire);
    }
}
