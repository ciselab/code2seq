# from nltk.tokenize import word_tokenize
# from nltk.translate.bleu_score import sentence_bleu
# from nltk.translate.bleu_score import SmoothingFunction
# import scipy.stats as stats
# from cliffs_delta import cliffs_delta
from glob import glob
import os

# Global variable to hold the data for each experiment
experiment_data = []

class ExperimentResult:
    """ Class to hold the output of a single experiment for easier managmenet """
    def __init__(self, references, predictions, stats, loss) -> None:
        self.references = references
        self.predictions = predictions
        self.stats = stats
        self.loss = loss
 
def main():
    """ Main program """
    # Load the data by creating an ExperimentResults object for each experiment
    load_data()

    # Process the gathered data.
    run_statistical_tests()
    generate_graphs()

    return 0

def load_data():

    for dir in glob("../models/exp_*/"):
        # ref file are the labels
        # log file is the generated prediction
        references = open(dir + "ref.txt").readlines() if os.path.isfile(dir + "ref.txt") else []
        predictions = open(dir + "pred.txt").readlines() if os.path.isfile(dir + "log.txt") else []

        # the format of stats is (separated by whitespace):
        # epoch, accuracy, precision, recall, f1 
        stats = open(dir + "stats.txt").readlines() if os.path.isfile(dir + "stats.txt") else []

        # the format of loss is (separated by whitespace):
        # batch number, average loss, throughput
        loss = open(dir + "loss.txt").readlines() if os.path.isfile(dir + "loss.txt") else []

        res = ExperimentResult(references, predictions, stats, loss)
        experiment_data.append(res)
    print("====== LOADED EXPERIMENT DATA ======")

def run_statistical_tests():

    # TODO: Implement methods for statistical tests 
    # TODO: For Leonhard: this is how I calculated statistics for my results. I leave it as a reference. - Balys.

    # for i, ref in enumerate(references):
    #     ref_tokens = set(word_tokenize(ref))
    #     com_pred_tokens = set(word_tokenize(predictions_com[i]))
    #     no_com_pred_tokens = set(word_tokenize(predictions_no_com[i]))
    #
    #     # Jaccard distance
    #     jac = 1 - (len(ref_tokens & com_pred_tokens) / len(ref_tokens | com_pred_tokens))
    #     jac_no = 1 - (
    #         len(ref_tokens & no_com_pred_tokens) / len(ref_tokens | no_com_pred_tokens)
    #     )
    #     com_jac.append(jac)
    #     no_com_jac.append(jac_no)
    #
    #     # BLEU score
    #     bleu = sentence_bleu(
    #         ref_tokens, com_pred_tokens, smoothing_function=SmoothingFunction().method1
    #     )
    #     bleu_no = sentence_bleu(
    #         ref_tokens, no_com_pred_tokens, smoothing_function=SmoothingFunction().method1
    #     )
    #     com_bleu.append(bleu)
    #     no_com_bleu.append(bleu_no)
    #
    # print(
    #     "Rank Sum with BLEU:\n", stats.ranksums(com_bleu, no_com_bleu, alternative="less")
    # )
    # print(
    #     "Rank Sum  with Jaccard Distance:\n",
    #     stats.ranksums(com_jac, no_com_jac, alternative="less"),
    # )
    #
    # print("BLEU Cliff Delta:", cliffs_delta(com_bleu, no_com_bleu))
    # print("Jaccard Cliff Delta:", cliffs_delta(com_jac, no_com_jac))

    pass


def generate_graphs():
    # TODO: implement methods for plot generation
    pass

if __name__ == "__main__":
    main()
