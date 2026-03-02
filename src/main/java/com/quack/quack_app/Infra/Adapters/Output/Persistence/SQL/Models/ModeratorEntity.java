package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "moderators")
@Getter
@Setter
@NoArgsConstructor
public class ModeratorEntity extends BaseEntity {
}
