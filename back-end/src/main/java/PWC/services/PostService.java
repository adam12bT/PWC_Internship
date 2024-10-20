package PWC.services;

import PWC.entities.Comment;
import PWC.entities.Post;
import PWC.entities.User;
import PWC.repository.CommentRepository;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(PostService.class.getName());

    public PostService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Comment addComment(Long postId, Long userId , String content) {
        Post post = postRepository.findById(postId).get();
        Comment comment = new Comment();

        comment.setUser(userRepository.findById(userId).get());

        comment.setPost(post);
        comment.setContent(content);
        return commentRepository.save(comment);

    }
    public Comment saveComment(Comment comment) {
        LOGGER.info("Saving comment: " + comment);

        return commentRepository.save(comment);
    }
    public Comment createComment(Long postId, Long userId, Comment commentaire) {
        Post post = postRepository.findById(postId).get();
        User  user= userRepository.findById(userId).get();

        commentaire.setPost(post);
        commentaire.setUser(user);
        post.getComments().add(commentaire);
        return commentRepository.save(commentaire);
    }
    public List<Post> getAllPostsWithComments() {
        List<Post> posts = postRepository.findAll();

        // Initialize comments collection if FetchType is LAZY
        for (Post post : posts) {
            if (post.getComments() != null) {
                LOGGER.info("Comments for Post ID {}: {}"+post.getId()+post.getComments().size());
            } else {
                LOGGER.info("No comments for Post ID {}"+ post.getId());
            }
        }

        return posts;
    }
}
