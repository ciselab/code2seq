from argparse import ArgumentParser
import nltk
from nltk.tokenize import word_tokenize
from nltk.translate.bleu_score import sentence_bleu
from nltk.translate.bleu_score import SmoothingFunction
import scipy.stats as stats
from cliffs_delta import cliffs_delta

nltk.download("punkt")

# Get the name of the trained model from args
parser = ArgumentParser()
parser.add_argument(
    "-d",
    "--dataset",
    dest="model_name",
    required=True,
)

args = parser.parse_args()
model_name = args.model_name

references = open(f"../models/trained_{model_name}/ref.txt").readlines()
predictions  = open(f"../models/trained_{model_name}/pred.txt").readlines()

jaccard_distance = []
bleu_score = []

for i, ref in enumerate(references):
    ref_tokens = set(word_tokenize(ref))
    pred_tokens = set(word_tokenize(predictions[i]))

    # Jaccard distance
    jac = 1 - (len(ref_tokens & pred_tokens) / len(ref_tokens | pred_tokens))
    jaccard_distance.append(jac)

    # BLEU score
    bleu = sentence_bleu(
        ref_tokens, pred_tokens, smoothing_function=SmoothingFunction().method1
    )
    bleu_score.append(bleu)

print("Jaccard distances: " )
print(jaccard_distance)
print("BLEU scores: ")
print(bleu_score)
