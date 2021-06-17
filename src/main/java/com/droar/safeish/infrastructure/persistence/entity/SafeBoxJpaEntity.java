package com.droar.safeish.infrastructure.persistence.entity;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Safebox domain entity
 * 
 * @author droar
 *
 */
@Data
@Entity
@Table(name = "SFI_SAFEBOX")
@NoArgsConstructor
public class SafeBoxJpaEntity implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7325849697684142437L;

  /** The id. */
  @Id
  @Access(value = AccessType.PROPERTY)
  @Column(nullable = false, unique = true, name = "SBX_ID")
  @SequenceGenerator(name = "safeBoxKey_generator", sequenceName = "public.seq_id_safebox", allocationSize = 1)
  @GeneratedValue(generator = "safeBoxKey_generator", strategy = GenerationType.SEQUENCE)
  private Integer id;

  /** The uuid. */
  @Column(name = "SBX_UUID")
  private String uuid;

  /** The safebox name. */
  @Column(name = "SBX_NAME")
  private String name;

  /** The safebox password. */
  @Column(name = "SBX_PASSWORD")
  private String password;
  
  /** The num open attempts. */
  @Column(name = "SBX_OPEN_ATTEMPTS")
  private Integer numOpenAttempts;
  
  /** The blocked. */
  @Column(name = "SBX_BLOCKED")
  private Boolean blocked;


  /**
   * Instantiates a new safe box jpa entity.
   *
   * @param uuid the uuid
   * @param name the name
   * @param password the password
   */
  public SafeBoxJpaEntity(String uuid, String name, String password) {
    this.uuid = uuid;
    this.name = name;
    this.password = password;
    
    // Default creation values
    this.numOpenAttempts = 0;
    this.blocked = Boolean.FALSE;
  }
}
