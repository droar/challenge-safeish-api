package com.droar.safeish.commons;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

/**
 * The Class Argon2CustomHasher.
 *
 * Gives argo2 hashing functionality, it will
 * be used to hash items on the safe box
 *
 * @author droar
 */
public class Argon2CustomHasher {

  /** The Constant DEFAULT_HASH_LENGTH. */
  private static final int DEFAULT_HASH_LENGTH = 32;
  
  /** The Constant DEFAULT_PARALLELISM. */
  private static final int DEFAULT_PARALLELISM = 1;
  
  /** The Constant DEFAULT_MEMORY. */
  private static final int DEFAULT_MEMORY = 16;
  
  /** The Constant DEFAULT_ITERATIONS. */
  private static final int DEFAULT_ITERATIONS = 2;
  
  /** The hash length. */
  private int hashLength;
  
  /** The parallelism. */
  private int parallelism;
  
  /** The memory. */
  private int memory;
  
  /** The iterations. */
  private int iterations;
  
  /** The salt. */
  private String salt;

  /**
   * Instantiates a new argon 2 password hasher.
   *
   * @param hashLength bytes del hash
   * @param parallelism the parallelism
   * @param memory the memory
   * @param iterations the iterations
   * @param salt Salt con la que se calculara el hash
   */
  public Argon2CustomHasher(int hashLength, int parallelism, int memory, int iterations, String salt) {

    this.hashLength = hashLength;
    this.parallelism = parallelism;
    this.memory = memory;
    this.iterations = iterations;
    this.salt = salt;
  }

  /**
   * Instantiates a new argon 2 password hasher.
   *
   * @param salt Salt para calcular el hash
   */
  public Argon2CustomHasher(String salt) {
    this(DEFAULT_HASH_LENGTH, DEFAULT_PARALLELISM, DEFAULT_MEMORY, DEFAULT_ITERATIONS, salt);
  }

  /**
   * Cipher a string sequence
   *
   * @param rawPassword the raw password
   * @return the byte[]
   */
  public byte[] cipher(CharSequence stringToCipher) {
    byte[] hash = new byte[hashLength];

    Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
        .withSalt(salt.getBytes())
        .withParallelism(parallelism)
        .withMemoryAsKB(memory)
        .withIterations(iterations)
        .build();
    
    Argon2BytesGenerator generator = new Argon2BytesGenerator();
    generator.init(params);
    generator.generateBytes(stringToCipher.toString().toCharArray(), hash);

    return hash;
  }
}
