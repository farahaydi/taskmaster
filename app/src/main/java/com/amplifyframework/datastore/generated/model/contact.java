package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the contact type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "contacts", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class contact implements Model {
  public static final QueryField ID = field("contact", "id");
  public static final QueryField EMAIL = field("contact", "email");
  public static final QueryField FULL_NAME = field("contact", "fullName");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String email;
  private final @ModelField(targetType="String") String fullName;
  private final @ModelField(targetType="Task") @HasMany(associatedWith = "contactPerson", type = Task.class) List<Task> tasks = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getEmail() {
      return email;
  }
  
  public String getFullName() {
      return fullName;
  }
  
  public List<Task> getTasks() {
      return tasks;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private contact(String id, String email, String fullName) {
    this.id = id;
    this.email = email;
    this.fullName = fullName;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      contact contact = (contact) obj;
      return ObjectsCompat.equals(getId(), contact.getId()) &&
              ObjectsCompat.equals(getEmail(), contact.getEmail()) &&
              ObjectsCompat.equals(getFullName(), contact.getFullName()) &&
              ObjectsCompat.equals(getCreatedAt(), contact.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), contact.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getEmail())
      .append(getFullName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("contact {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("fullName=" + String.valueOf(getFullName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static EmailStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static contact justId(String id) {
    return new contact(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      email,
      fullName);
  }
  public interface EmailStep {
    BuildStep email(String email);
  }
  

  public interface BuildStep {
    contact build();
    BuildStep id(String id);
    BuildStep fullName(String fullName);
  }
  

  public static class Builder implements EmailStep, BuildStep {
    private String id;
    private String email;
    private String fullName;
    public Builder() {
      
    }
    
    private Builder(String id, String email, String fullName) {
      this.id = id;
      this.email = email;
      this.fullName = fullName;
    }
    
    @Override
     public contact build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new contact(
          id,
          email,
          fullName);
    }
    
    @Override
     public BuildStep email(String email) {
        Objects.requireNonNull(email);
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String email, String fullName) {
      super(id, email, fullName);
      Objects.requireNonNull(email);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder fullName(String fullName) {
      return (CopyOfBuilder) super.fullName(fullName);
    }
  }
  

  public static class contactIdentifier extends ModelIdentifier<contact> {
    private static final long serialVersionUID = 1L;
    public contactIdentifier(String id) {
      super(id);
    }
  }
  
}
