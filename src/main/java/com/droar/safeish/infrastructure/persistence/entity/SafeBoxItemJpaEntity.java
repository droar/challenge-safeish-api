package com.droar.safeish.infrastructure.persistence.entity;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "SFI_SAFEBOX_ITEMS")
@NoArgsConstructor
public class SafeBoxItemJpaEntity implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -7470858987607048118L;

  @Id
  @Access(value = AccessType.PROPERTY)
  @Column(nullable = false, unique = true, name = "SBC_ITEM_ID")
  @SequenceGenerator(name = "safeBoxItemKey_generator", sequenceName = "public.seq_id_safebox_item", allocationSize = 1)
  @GeneratedValue(generator = "safeBoxItemKey_generator", strategy = GenerationType.SEQUENCE)
  private Integer id;

  /** The safe box id. */
  @JoinColumn(name = "SBC_SAFEBOX_UUID", referencedColumnName = "SBX_UUID")
  @Column(name = "SBC_SAFEBOX_UUID")
  private String safeBoxUuid;

  /** The item value. */
  @Column(name = "SBC_ITEM_VALUE")
  private String itemValue;

  /**
   * Instantiates a new safe box item jpa entity.
   * 
   * @param safeBoxUuid
   * @param itemValue
   */
  public SafeBoxItemJpaEntity(String safeBoxUuid, String itemValue) {
    this.safeBoxUuid = safeBoxUuid;
    this.itemValue = itemValue;
  }
}
