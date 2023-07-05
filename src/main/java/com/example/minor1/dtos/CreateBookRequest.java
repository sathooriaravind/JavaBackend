package com.example.minor1.dtos;

import com.example.minor1.models.Author;
import com.example.minor1.models.Book;
import com.example.minor1.models.Genre;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter@Setter@Builder@NoArgsConstructor@AllArgsConstructor
public class CreateBookRequest {

        @NotBlank
        private String name;

        @NotNull // @NotBlank works only with pre-defined data types
        private Genre genre;

        @NotBlank
        private String authorName;

        @NotBlank
        private String authorEmail;

        public Book toBook(){
            return Book.builder()
                    .name(this.name)
                    .genre(this.genre)
                    .author(this.toAuthor())
                    .build();
        }

        public Author toAuthor(){
            return Author.builder()
                    .name(this.authorName)
                    .email(this.authorEmail)
                    .build();
        }
}
