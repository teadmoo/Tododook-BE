package dooks.tododook.service;



import dooks.tododook.model.TodoEntity;
import dooks.tododook.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public Optional<TodoEntity> create(final TodoEntity entity){
        validate(entity);
        repository.save(entity);
        return repository.findById(entity.getId());
    }

    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    public Optional<TodoEntity> update(final TodoEntity entity){
        validate(entity);
        if(repository.existsById(entity.getId())){
            repository.save(entity);
        }
        else throw new RuntimeException("Unknown id");

        return repository.findById(entity.getId());
    }

    public String delete(final String id){
        if(repository.existsById(id))repository.deleteById(id);
        else throw new RuntimeException("is does not exist");

        return "Deleted";
    }

    public Optional<TodoEntity> updateTodo(final TodoEntity entity){
        validate(entity);

        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo->{
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });

        return repository.findById(entity.getId());
    }



    public void validate(final TodoEntity entity){
        if(entity == null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getUserId()==null){
            log.warn("UnKnown user.");
            throw new RuntimeException("UnKnown user.");
        }
    }
}
