CREATE TYPE "quiz_level_type" AS ENUM ('NONE', 'BEGINNER', 'ADVANCED', 'EXPERT');

-- Create table for Quiz Item
CREATE TABLE "quiz_item"
(
    "question_id"    TEXT PRIMARY KEY,
    CHECK (length("question_id") < 64),
    "question"       TEXT              NOT NULL CHECK (length("question") <= 1024),
    "level"          "quiz_level_type" NOT NULL,
    "correct_answer" TEXT              NOT NULL CHECK (length("correct_answer") <= 1024),
    "explanation"    TEXT CHECK (length("explanation") <= 2048),
    "lock"           TEXT              NOT NULL
        CONSTRAINT questions_lock_length_ctr CHECK (length(lock) < 64)
);

-- Create table for Answer Option
CREATE TABLE "answer_option"
(
    "answer_option_id" SERIAL PRIMARY KEY,
    "option_id"        TEXT NOT NULL CHECK (length(option_id) < 64),
    "option_text"      TEXT NOT NULL CHECK (length("option_text") <= 1024),
    "question_id"      TEXT NOT NULL,

    CONSTRAINT fk_answer_option_quiz_item FOREIGN KEY ("question_id") REFERENCES "quiz_item" ("question_id") ON DELETE CASCADE
);
